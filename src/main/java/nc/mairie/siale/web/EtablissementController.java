package nc.mairie.siale.web;

import nc.mairie.siale.domain.Etablissement;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/etablissements")
@Controller
@RooWebScaffold(path = "etablissements", formBackingObject = Etablissement.class)
public class EtablissementController {
}
