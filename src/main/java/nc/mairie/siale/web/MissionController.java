package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.Bareme;
import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Etablissement;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.MissionAction;
import nc.mairie.siale.domain.MissionActivite;
import nc.mairie.siale.domain.MissionDocument;
import nc.mairie.siale.domain.Notation;
import nc.mairie.siale.domain.Param;
import nc.mairie.siale.technique.RisqueEtablissement;
import nc.mairie.siale.technique.TypeEtablissement;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/missions")
@Controller
public class MissionController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Mission mission, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, mission);
            return "missions/create";
        }
        uiModel.asMap().clear();
        mission.persist();
        return "redirect:/missions/" + encodeUrlPathSegment(mission.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Mission());
        return "missions/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("mission", Mission.findMission(id));
        uiModel.addAttribute("itemId", id);
        return "missions/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("missions", Mission.findMissionEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Mission.countMissions() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("missions", Mission.findAllMissions(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "missions/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Mission mission, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, mission);
            return "missions/update";
        }
        uiModel.asMap().clear();
        mission.merge();
        return "redirect:/missions/" + encodeUrlPathSegment(mission.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Mission.findMission(id));
        return "missions/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Mission mission = Mission.findMission(id);
        mission.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/missions";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("mission_dateintervention_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("mission_dateprevue_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("mission_dureeprevuerdv_date_format", DateTimeFormat.patternForStyle("-S", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("mission_datenotation_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Mission mission) {
        uiModel.addAttribute("mission", mission);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("baremes", Bareme.findAllBaremes());
        uiModel.addAttribute("controleursiales", ControleurSIALE.findAllControleurSIALEs());
        uiModel.addAttribute("etablissements", Etablissement.findAllEtablissements());
        uiModel.addAttribute("missionactions", MissionAction.findAllMissionActions());
        uiModel.addAttribute("missionactivites", MissionActivite.findAllMissionActivites());
        uiModel.addAttribute("missiondocuments", MissionDocument.findAllMissionDocuments());
        uiModel.addAttribute("notations", Notation.findAllNotations());
        uiModel.addAttribute("params", Param.findAllParams());
        uiModel.addAttribute("risqueetablissements", Arrays.asList(RisqueEtablissement.values()));
        uiModel.addAttribute("typeetablissements", Arrays.asList(TypeEtablissement.values()));
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
