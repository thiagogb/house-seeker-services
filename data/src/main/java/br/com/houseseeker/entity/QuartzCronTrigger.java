package br.com.houseseeker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "qrtz_cron_triggers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class QuartzCronTrigger implements Serializable {

    @Serial
    private static final long serialVersionUID = 1901965734248327345L;

    @EmbeddedId
    private QuartzCronTriggerId id;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "time_zone_id")
    private String timeZoneId;

    public static final class QuartzCronTriggerId implements Serializable {

        @Serial
        private static final long serialVersionUID = 7594798592347408849L;

        @Column(name = "sched_name")
        private String schedulerName;

        @Column(name = "trigger_name")
        private String triggerName;

        @Column(name = "trigger_group")
        private String triggerGroup;

    }

}
