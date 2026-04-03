package com.akshita.jad.grpcweb.grpc.service;

import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandles;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.akshita.jad.core.command.express.Express;
import com.akshita.jad.core.command.express.ExpressException;
import com.akshita.jad.core.command.express.ExpressFactory;
import com.akshita.jad.core.util.Constants;
import com.akshita.jad.grpcweb.grpc.objectUtils.JavaObjectConverter;
import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.common.VmToolUtils;

import jad.VmTool;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;
import com.akshita.jad.grpcweb.grpc.observer.impl.JADStreamObserverImpl;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.jad.api.ObjectServiceGrpc.ObjectServiceImplBase;
import io.jad.api.JADServices.JavaObject;
import io.jad.api.JADServices.ObjectQuery;
import io.jad.api.JADServices.ObjectQueryResult;
import io.jad.api.JADServices.ObjectQueryResult.Builder;

public class ObjectService extends ObjectServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private VmTool vmTool;
    private Instrumentation inst;

    private GrpcJobController grpcJobController;


    public ObjectService(GrpcJobController grpcJobController, String libDir) {
        this.inst = grpcJobController.getInstrumentation();
        this.grpcJobController = grpcJobController;

        try {
            String detectLibName = VmToolUtils.detectLibName();
            String vmToolLibPath = Paths.get(libDir, detectLibName).toString();

            vmTool = VmTool.getInstance(vmToolLibPath);
        } catch (Throwable e) {
            logger.error("init vmtool error", e);
        }
    }

    @Override
    public void query(ObjectQuery query, StreamObserver<ObjectQueryResult> responseObserver) {
        if (vmTool == null) {
            throw Status.UNAVAILABLE.withDescription("vmtool can not work").asRuntimeException();
        }
        JADStreamObserver<ObjectQueryResult> jadStreamObserver = new JADStreamObserverImpl<>(responseObserver, null,grpcJobController);
        String className = query.getClassName();
        String classLoaderHash = query.getClassLoaderHash();
        String classLoaderClass = query.getClassLoaderClass();
        int limit = query.getLimit();
        int depth = query.getDepth();
        String express = query.getExpress();
        String resultExpress = query.getResultExpress();

        //  class name ，jvm  class，
        if (isEmpty(classLoaderHash) && isEmpty(classLoaderClass)) {
            List<Class<?>> foundClassList = new ArrayList<>();
            for (Class<?> clazz : inst.getAllLoadedClasses()) {
                if (clazz.getName().equals(className)) {
                    foundClassList.add(clazz);
                }
            }

            // 
            if (foundClassList.size() == 0) {
                jadStreamObserver.onNext(ObjectQueryResult.newBuilder().setSuccess(false)
                        .setMessage("can not find class: " + className).build());
                jadStreamObserver.onCompleted();
                return;
            } else if (foundClassList.size() > 1) {
                String message = "found more than one class: " + className;
                jadStreamObserver.onNext(ObjectQueryResult.newBuilder().setSuccess(false).setMessage(message).build());
                jadStreamObserver.onCompleted();
                return;
            } else { //  
                Object[] instances = vmTool.getInstances(foundClassList.get(0), limit);
                Builder builder = ObjectQueryResult.newBuilder().setSuccess(true);
                /**
                 *  express
                 */
                Object value = null;
                if (!isEmpty(express)) {
                    Express unpooledExpress = ExpressFactory.unpooledExpress(foundClassList.get(0).getClassLoader());
                    try {
                        value = unpooledExpress.bind(new InstancesWrapper(instances)).get(express);
                    } catch (ExpressException e) {
                        logger.warn("ognl: failed execute express: " + express, e);
                    }
                }
                if(value != null && !isEmpty(resultExpress)){
                    try {
                        value = ExpressFactory.threadLocalExpress(value).bind(Constants.COST_VARIABLE, 0.0).get(resultExpress);
                    } catch (ExpressException e) {
                        logger.warn("ognl: failed execute result express: " + express, e);
                    }
                }
                JavaObject javaObject = JavaObjectConverter.toJavaObjectWithExpand(value, depth);
                builder.addObjects(javaObject);
                jadStreamObserver.onNext(builder.build());
                jadStreamObserver.onCompleted();
                return;
            }
        }

        //  classloader hash  classloader className

        Class<?> foundClass = null;

        for (Class<?> clazz : inst.getAllLoadedClasses()) {
            if (!clazz.getName().equals(className)) {
                continue;
            }

            ClassLoader classLoader = clazz.getClassLoader();

            if (classLoader == null) {
                continue;
            }

            if (!isEmpty(classLoaderHash)) {
                String hex = Integer.toHexString(classLoader.hashCode());
                if (classLoaderHash.equals(hex)) {
                    foundClass = clazz;
                    break;
                }
            }

            if (!isEmpty(classLoaderClass) && classLoaderClass.equals(classLoader.getClass().getName())) {
                foundClass = clazz;
                break;
            }
        }
        // 
        if (foundClass == null) {
            jadStreamObserver.onNext(ObjectQueryResult.newBuilder().setSuccess(false)
                    .setMessage("can not find class: " + className).build());
            jadStreamObserver.onCompleted();
            return;
        }

        Object[] instances = vmTool.getInstances(foundClass, limit);
        Builder builder = ObjectQueryResult.newBuilder().setSuccess(true);
//        for (Object obj : instances) {
//            JavaObject javaObject = JavaObjectConverter.toJavaObjectWithExpand(obj, depth);
//            builder.addObjects(javaObject);
//        }

        Object value = null;
        if (!isEmpty(express)) {
            Express unpooledExpress = ExpressFactory.unpooledExpress(foundClass.getClassLoader());
            try {
                value = unpooledExpress.bind(new InstancesWrapper(instances)).get(express);
            } catch (ExpressException e) {
                logger.warn("ognl: failed execute express: " + express, e);
            }
        }
        if(value != null && !isEmpty(resultExpress)){
            try {
                value = ExpressFactory.threadLocalExpress(value).bind(Constants.COST_VARIABLE, 0.0).get(resultExpress);
            } catch (ExpressException e) {
                logger.warn("ognl: failed execute result express: " + express, e);
            }
        }
        JavaObject javaObject = JavaObjectConverter.toJavaObjectWithExpand(value, depth);
        builder.addObjects(javaObject);
        jadStreamObserver.onNext(builder.build());
        jadStreamObserver.onCompleted();
    }

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    static class InstancesWrapper {
        Object instances;

        public InstancesWrapper(Object instances) {
            this.instances = instances;
        }

        public Object getInstances() {
            return instances;
        }

        public void setInstances(Object instances) {
            this.instances = instances;
        }
    }

}
