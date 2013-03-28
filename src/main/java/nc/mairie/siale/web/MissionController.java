package nc.mairie.siale.web;

import nc.mairie.siale.domain.Mission;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/missions")
@Controller
@RooWebScaffold(path = "missions", formBackingObject = Mission.class)
public class MissionController {
}
