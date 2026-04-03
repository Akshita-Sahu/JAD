package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.SystemEnvModel;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * @author gongdewei 2020/4/2
 */
public class SystemEnvView extends ResultView<SystemEnvModel> {

    @Override
    public void draw(CommandProcess process, SystemEnvModel result) {
        process.write(ViewRenderUtil.renderKeyValueTable(result.getEnv(), process.width()));
    }

}
