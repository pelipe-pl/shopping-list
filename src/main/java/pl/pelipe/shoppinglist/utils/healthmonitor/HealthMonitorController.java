package pl.pelipe.shoppinglist.utils.healthmonitor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
public class HealthMonitorController {

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public String healthCheck(Model model) {

        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        long startTimeMillis = runtimeBean.getStartTime();
        Date startTime = new Date(startTimeMillis);
        long upTimeMillis = runtimeBean.getUptime();
        String upTimeHumanReadable = getDuration(upTimeMillis);

        model.addAttribute("startDate", startTime);
        model.addAttribute("upTime", upTimeHumanReadable);

        return "health";
    }

    private static String getDuration(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        } else {
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            millis -= TimeUnit.DAYS.toMillis(days);
            long hours = TimeUnit.MILLISECONDS.toHours(millis);
            millis -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
            millis -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

            StringBuilder sb = new StringBuilder(64);
            sb.append(days);
            sb.append("d ");
            sb.append(hours);
            sb.append("h ");
            sb.append(minutes);
            sb.append("m ");
            sb.append(seconds);
            sb.append("s");

            return (sb.toString());
        }
    }
}