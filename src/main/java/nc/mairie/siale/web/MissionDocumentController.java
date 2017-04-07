package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.MissionDocument;
import nc.mairie.siale.domain.Param;
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

@RequestMapping("/missiondocuments")
@Controller
public class MissionDocumentController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid MissionDocument missionDocument, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, missionDocument);
            return "missiondocuments/create";
        }
        uiModel.asMap().clear();
        missionDocument.persist();
        return "redirect:/missiondocuments/" + encodeUrlPathSegment(missionDocument.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new MissionDocument());
        return "missiondocuments/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("missiondocument", MissionDocument.findMissionDocument(id));
        uiModel.addAttribute("itemId", id);
        return "missiondocuments/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("missiondocuments", MissionDocument.findMissionDocumentEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) MissionDocument.countMissionDocuments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("missiondocuments", MissionDocument.findAllMissionDocuments(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "missiondocuments/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid MissionDocument missionDocument, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, missionDocument);
            return "missiondocuments/update";
        }
        uiModel.asMap().clear();
        missionDocument.merge();
        return "redirect:/missiondocuments/" + encodeUrlPathSegment(missionDocument.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MissionDocument.findMissionDocument(id));
        return "missiondocuments/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MissionDocument missionDocument = MissionDocument.findMissionDocument(id);
        missionDocument.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/missiondocuments";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("missionDocument_datedocument_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, MissionDocument missionDocument) {
        uiModel.addAttribute("missionDocument", missionDocument);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("missions", Mission.findAllMissions());
        uiModel.addAttribute("params", Param.findAllParams());
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
