package org.example.smartroute.optimizier.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.Tour;
import org.example.smartroute.optimizier.TourOptimizer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@ConditionalOnProperty(name = "optimizer.default", havingValue = "ai")
public class AIOptimizer implements TourOptimizer {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AIOptimizer(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public List<Delivery> optimizerTour(Tour tour) {
        try {
            String prompt = buildPrompt(tour);

            String content = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            return parseDeliveries(content, tour);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l’analyse de la réponse du LLM", e);
        }
    }

    private String buildPrompt(Tour tour) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                Vous êtes un expert en optimisation de tournées logistiques.
                Objectif : réorganiser la liste des livraisons de cette tournée dans le meilleur ordre possible afin de minimiser la distance totale.
                Utilisez la latitude et la longitude pour estimer les distances.
                Retournez uniquement un JSON au format suivant, sans aucune explication :
                {
                  "orderedDeliveries": [
                    {"id": 1, "order": 1, "reason": "Proche du point de départ"},
                    {"id": 5, "order": 2, "reason": "Dans la même direction"},
                    ...
                  ]
                }
                """);
        sb.append("\n\n");
        sb.append("Données d'entrée :\n");
        sb.append("{\"deliveries\": [");

        List<Delivery> deliveries = tour.getDeliveries();
        for (int i = 0; i < deliveries.size(); i++) {
            Delivery d = deliveries.get(i);
            sb.append("{")
                    .append("\"id\":").append(d.getId()).append(",")
                    .append("\"address\":\"").append(d.getAddress()).append("\",")
                    .append("\"latitude\":").append(d.getLatitude()).append(",")
                    .append("\"longitude\":").append(d.getLongitude()).append(",")
                    .append("\"weight\":").append(d.getWeight()).append(",")
                    .append("\"volume\":").append(d.getVolume()).append(",")
                    .append("\"timeWindow\":\"").append(d.getTimeWindow()).append("\"")
                    .append("}");
            if (i < deliveries.size() - 1) sb.append(",");
        }
        sb.append("]}");

        return sb.toString();
    }

    private List<Delivery> parseDeliveries(String json, Tour tour) {
        List<Delivery> ordered = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode arr = root.path("orderedDeliveries");

            if (arr.isArray()) {
                Iterator<JsonNode> it = arr.elements();
                while (it.hasNext()) {
                    JsonNode node = it.next();
                    Long deliveryId = node.path("id").asLong();

                    // Associer l'ID reçu à la livraison originale de la tournée
                    Delivery existing = tour.getDeliveries()
                            .stream()
                            .filter(d -> d.getId().equals(deliveryId))
                            .findFirst()
                            .orElse(null);

                    if (existing != null) {
                        ordered.add(existing);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Impossible d’analyser le JSON retourné par le LLM", e);
        }
        return ordered;
    }
}
