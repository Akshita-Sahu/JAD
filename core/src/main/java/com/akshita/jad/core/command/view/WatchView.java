package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.ObjectVO;
import com.akshita.jad.core.command.model.WatchModel;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.DateUtils;
import com.akshita.jad.core.util.StringUtils;
import com.akshita.jad.core.view.ObjectView;

/**
 * Term view for WatchModel
 *
 * @author gongdewei 2020/3/27
 */
public class WatchView extends ResultView<WatchModel> {

    @Override
    public void draw(CommandProcess process, WatchModel model) {
        ObjectVO objectVO = model.getValue();
        int sizeLimit = ObjectView.normalizeMaxObjectLength(model.getSizeLimit());
        String result = StringUtils.objectToString(
                objectVO.needExpand() ? new ObjectView(sizeLimit, objectVO).draw() : objectVO.getObject());

        StringBuilder sb = new StringBuilder();
        sb.append("method=").append(model.getClassName()).append(".").append(model.getMethodName())
                .append(" location=").append(model.getAccessPoint()).append("\n");
        sb.append("ts=").append(DateUtils.formatDateTime(model.getTs()))
                .append("; [cost=").append(model.getCost()).append("ms] result=").append(result).append("\n");

        process.write(sb.toString());
    }
}
