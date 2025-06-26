package org.example.zgadnij_slowo.Score;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

public class Time {
    private Timeline timeline;
    private IntegerProperty timeSeconds;

    public Time(int seconds, Runnable onTick, Runnable onFinished) {
        timeSeconds = new SimpleIntegerProperty(seconds);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            int current = timeSeconds.get();
            if (current > 0) {
                timeSeconds.set(current - 1);
                onTick.run();
                if (timeSeconds.get() == 0) {
                    stop();
                    onFinished.run();
                }
            }
        }));
        timeline.setCycleCount(seconds);
    }
    public void start() {
        if (timeline != null) {
            timeline.playFromStart();
        }
    }
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public IntegerProperty timeSecondsProperty() {
        return timeSeconds;
    }
}