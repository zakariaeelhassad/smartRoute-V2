package org.example.smartroute.entities.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.smartroute.entities.enums.AlgorithmType;
import org.example.smartroute.entities.enums.TourStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tours")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private Double totalDistance;

    @ManyToOne
    @JoinColumn(name = "warehouse_id" , nullable = false)
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "vehicle_id" , nullable = false)
    private Vehicle vehicle;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Delivery> deliveries;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TourStatus status = TourStatus.PLANNED;
}
