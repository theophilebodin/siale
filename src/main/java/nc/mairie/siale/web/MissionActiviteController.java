package nc.mairie.siale.web;

import nc.mairie.siale.domain.MissionActivite;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/missionactivites")
@Controller
@RooWebScaffold(path = "missionactivites", formBackingObject = MissionActivite.class)
public class MissionActiviteController {
}
