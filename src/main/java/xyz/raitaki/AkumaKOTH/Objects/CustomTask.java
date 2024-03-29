package xyz.raitaki.AkumaKOTH.Objects;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.AkumaKOTH.AkumaKOTH;

import java.util.function.Consumer;

public class CustomTask {

    private int delay;
    private int period;
    private BukkitRunnable runnable;

    public CustomTask( int delay, int period){
        this.delay = delay;
        this.period = period;
    }

    public void start(Consumer<BukkitRunnable> taskConsumer, boolean async){
        this.runnable = new BukkitRunnable(){
            @Override
            public void run() {
                taskConsumer.accept(this);
            }
        };
        if(async)
            if(period == 0)
                runnable.runTaskLaterAsynchronously(AkumaKOTH.getInstance(), delay);
            else
                runnable.runTaskTimerAsynchronously(AkumaKOTH.getInstance(), delay, period);
        else
            if(period == 0)
                runnable.runTaskLater(AkumaKOTH.getInstance(), delay);
            else
                runnable.runTaskTimer(AkumaKOTH.getInstance(), delay, period);
    }

    public int getDelay() {
        return delay;
    }

    public int getPeriod() {
        return period;
    }

    public BukkitRunnable getRunnable() {
        return runnable;
    }
}
