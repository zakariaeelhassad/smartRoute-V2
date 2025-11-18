package org.example.smartroute.optimizier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
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

        System.out.println("🧠 Registered optimizers: " +
                this.optimizers.keySet());
    }

    public TourOptimizer getOptimizer(String type) {
        if (type == null || type.isEmpty()) {
            System.out.println("⚠️ optimizerType is null/empty, using default: " + defaultOptimizer);
            type = defaultOptimizer;
        }

        String key = type.toLowerCase() + "optimizer";
        TourOptimizer optimizer = optimizers.get(key);

        if (optimizer == null) {
            System.out.println("⚠️ No optimizer found for key '" + key + "', using default: " + defaultOptimizer);
            optimizer = optimizers.get(defaultOptimizer.toLowerCase() + "optimizer");
        } else {
            System.out.println("✅ Using optimizer: " + key + " -> " + optimizer.getClass().getSimpleName());
        }

        return optimizer;
    }


}
