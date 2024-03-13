package com.example.TheFit.feedback;

import com.example.TheFit.user.trainer.domain.Trainer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public abstract class FeedBack {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    public Trainer trainer;
    public String feedBack;
    public String rating;
    @CreationTimestamp
    public LocalDateTime createdTime;
    @UpdateTimestamp
    public LocalDateTime updatedTime;
}
