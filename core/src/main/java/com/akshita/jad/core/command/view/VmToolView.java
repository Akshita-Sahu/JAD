package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.ObjectVO;
import com.akshita.jad.core.command.model.VmToolModel;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.StringUtils;
import com.akshita.jad.core.view.ObjectView;

/**
 * 
 * @author hengyunabc 2022-04-24
 *
 */
public class VmToolView extends ResultView<VmToolModel> {
    @Override
    public void draw(CommandProcess process, VmToolModel model) {
        if (model.getMatchedClassLoaders() != null) {
            process.write("Matched classloaders: \n");
            ClassLoaderView.drawClassLoaders(process, model.getMatchedClassLoaders(), false);
            process.write("\n");
            return;
        }

        ObjectVO objectVO = model.getValue();
        String resultStr = StringUtils.objectToString(objectVO.needExpand() ? new ObjectView(objectVO).draw() : objectVO.getObject());
        process.write(resultStr).write("\n");
    }
}
