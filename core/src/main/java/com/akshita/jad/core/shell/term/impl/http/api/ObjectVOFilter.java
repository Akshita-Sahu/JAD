package com.akshita.jad.core.shell.term.impl.http.api;

import com.akshita_sahu.fastjson2.filter.ValueFilter;
import com.akshita.jad.core.command.model.ObjectVO;
import com.akshita.jad.core.util.StringUtils;
import com.akshita.jad.core.view.ObjectView;

/**
 * @author hengyunabc 2022-08-24
 *
 */
public class ObjectVOFilter implements ValueFilter {

    @Override
    public Object apply(Object object, String name, Object value) {
        if (value instanceof ObjectVO) {
            ObjectVO vo = (ObjectVO) value;
            String resultStr = StringUtils.objectToString(vo.needExpand() ? new ObjectView(vo).draw() : value);
            return resultStr;
        }
        return value;
    }

}
