
package org.example.jfranalyzerbackend.extractor;

import org.example.jfranalyzerbackend.model.StackTrace;
import org.example.jfranalyzerbackend.model.Task;
import org.example.jfranalyzerbackend.model.TaskCount;
import org.example.jfranalyzerbackend.model.TaskData;
import org.example.jfranalyzerbackend.model.jfr.RecordedEvent;
import org.example.jfranalyzerbackend.model.jfr.RecordedStackTrace;
import org.example.jfranalyzerbackend.model.jfr.RecordedThread;
import org.example.jfranalyzerbackend.util.StackTraceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * ，
 */
public abstract class CountExtractor extends Extractor {
    
    CountExtractor(JFRAnalysisContext context, List<String> interested) {
        super(context, interested);
    }

    /**
     * 
     * 
     */
    public static class CountMetrics extends TaskData {
        CountMetrics(RecordedThread thread) {
            super(thread);
        }

        long eventCount;
    }

    private final Map<Long, CountMetrics> threadCounts = new HashMap<>();

    private CountMetrics obtainCountMetrics(RecordedThread thread) {
        return threadCounts.computeIfAbsent(thread.getJavaThreadId(), 
            threadId -> new CountMetrics(thread));
    }

    protected void processCountEvent(RecordedEvent event) {
        RecordedStackTrace stackTrace = event.getStackTrace();
        if (stackTrace == null) {
            return;
        }

        CountMetrics metrics = obtainCountMetrics(event.getThread());
        initializeSamplesIfNeeded(metrics);
        
        metrics.getSamples().compute(stackTrace, (key, existingCount) -> 
            existingCount == null ? 1L : existingCount + 1L);
        metrics.eventCount += 1;
    }

    private void initializeSamplesIfNeeded(CountMetrics metrics) {
        if (metrics.getSamples() == null) {
            metrics.setSamples(new HashMap<>());
        }
    }

    public List<TaskCount> generateCountResults() {
        List<TaskCount> countResults = new ArrayList<>();
        
        for (CountMetrics metrics : this.threadCounts.values()) {
            if (metrics.eventCount == 0) {
                continue;
            }

            TaskCount countResult = createCountResult(metrics);
            countResults.add(countResult);
        }

        return sortCountsByValue(countResults);
    }

    private TaskCount createCountResult(CountMetrics metrics) {
        TaskCount result = new TaskCount();
        Task taskInfo = createTaskInfo(metrics.getThread());
        result.setTask(taskInfo);

        if (metrics.getSamples() != null) {
            result.setCount(metrics.eventCount);
            result.setSamples(transformSamples(metrics.getSamples()));
        }

        return result;
    }

    protected Task createTaskInfo(RecordedThread thread) {
        Task task = new Task();
        task.setId(thread.getJavaThreadId());
        task.setName(context.getThread(thread).getName());
        return task;
    }

    protected Map<StackTrace, Long> transformSamples(Map<RecordedStackTrace, Long> rawSamples) {
        return rawSamples.entrySet().stream()
                .collect(Collectors.toMap(
                    entry -> StackTraceUtil.build(entry.getKey(), context.getSymbols()),
                    Map.Entry::getValue,
                    Long::sum
                ));
    }

    private List<TaskCount> sortCountsByValue(List<TaskCount> counts) {
        counts.sort((first, second) -> {
            long difference = second.getCount() - first.getCount();
            return difference > 0 ? 1 : (difference == 0 ? 0 : -1);
        });
        return counts;
    }
}
