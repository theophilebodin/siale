package nc.mairie.siale.web;

import nc.mairie.siale.domain.NoteGroupe;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/notegroupes")
@Controller
@RooWebScaffold(path = "notegroupes", formBackingObject = NoteGroupe.class)
public class NoteGroupeController {
}
