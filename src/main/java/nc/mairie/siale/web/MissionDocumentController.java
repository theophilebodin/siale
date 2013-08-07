package nc.mairie.siale.web;

import nc.mairie.siale.domain.MissionDocument;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/missiondocuments")
@Controller
@RooWebScaffold(path = "missiondocuments", formBackingObject = MissionDocument.class)
public class MissionDocumentController {
}
