package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.MissionAction;
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

@RequestMapping("/missionactions")
@Controller
public class MissionActionController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid MissionAction missionAction, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, missionAction);
            return "missionactions/create";
        }
        uiModel.asMap().clear();
        missionAction.persist();
        return "redirect:/missionactions/" + encodeUrlPathSegment(missionAction.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new MissionAction());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Param.countParams() == 0) {
            dependencies.add(new String[] { "theAction", "params" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "missionactions/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("missionaction", MissionAction.findMissionAction(id));
        uiModel.addAttribute("itemId", id);
        return "missionactions/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("missionactions", MissionAction.findMissionActionEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) MissionAction.countMissionActions() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("missionactions", MissionAction.findAllMissionActions(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "missionactions/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid MissionAction missionAction, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, missionAction);
            return "missionactions/update";
        }
        uiModel.asMap().clear();
        missionAction.merge();
        return "redirect:/missionactions/" + encodeUrlPathSegment(missionAction.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MissionAction.findMissionAction(id));
        return "missionactions/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MissionAction missionAction = MissionAction.findMissionAction(id);
        missionAction.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/missionactions";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("missionAction_dateaction_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, MissionAction missionAction) {
        uiModel.addAttribute("missionAction", missionAction);
        addDateTimeFormatPatterns(uiModel);
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
