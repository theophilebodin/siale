package nc.mairie.siale.web;

import nc.mairie.siale.domain.MissionAction;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/missionactions")
@Controller
@RooWebScaffold(path = "missionactions", formBackingObject = MissionAction.class)
public class MissionActionController {
}
