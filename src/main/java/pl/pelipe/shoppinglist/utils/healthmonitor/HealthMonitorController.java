package pl.pelipe.shoppinglist.utils.healthmonitor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

@Controller
public class HealthMonitorController {

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public String healthCheck(Model model) {

        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        long startTime = runtimeBean.getStartTime();
        Date startDate = new Date(startTime);
        model.addAttribute("startDate", startDate);
        return "health";
    }
}