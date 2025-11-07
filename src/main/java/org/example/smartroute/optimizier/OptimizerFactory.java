package org.example.smartroute.optimizier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OptimizerFactory {

    private final Map<String, TourOptimizer> optimizers;
    private final String defaultOptimizer;

    public OptimizerFactory(List<TourOptimizer> optimizers,
                            @Value("${optimizer.default}") String defaultOptimizer) {
        this.optimizers = optimizers.stream()
                .collect(Collectors.toMap(o -> o.getClass().getSimpleName().toLowerCase(), o -> o));
        this.defaultOptimizer = defaultOptimizer;
    }

    public TourOptimizer getOptimizer(String type) {
        return optimizers.getOrDefault(type.toLowerCase(), optimizers.get(defaultOptimizer));
    }
}
