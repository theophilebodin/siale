package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.MissionActivite;
import nc.mairie.siale.domain.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/missionactivites")
@Controller
public class MissionActiviteController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid MissionActivite missionActivite, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, missionActivite);
            return "missionactivites/create";
        }
        uiModel.asMap().clear();
        missionActivite.persist();
        return "redirect:/missionactivites/" + encodeUrlPathSegment(missionActivite.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new MissionActivite());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Param.countParams() == 0) {
            dependencies.add(new String[] { "theActivite", "params" });
        }
        if (Mission.countMissions() == 0) {
            dependencies.add(new String[] { "theMission", "missions" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "missionactivites/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("missionactivite", MissionActivite.findMissionActivite(id));
        uiModel.addAttribute("itemId", id);
        return "missionactivites/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("missionactivites", MissionActivite.findMissionActiviteEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) MissionActivite.countMissionActivites() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("missionactivites", MissionActivite.findAllMissionActivites(sortFieldName, sortOrder));
        }
        return "missionactivites/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid MissionActivite missionActivite, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, missionActivite);
            return "missionactivites/update";
        }
        uiModel.asMap().clear();
        missionActivite.merge();
        return "redirect:/missionactivites/" + encodeUrlPathSegment(missionActivite.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MissionActivite.findMissionActivite(id));
        return "missionactivites/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MissionActivite missionActivite = MissionActivite.findMissionActivite(id);
        missionActivite.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/missionactivites";
    }

	void populateEditForm(Model uiModel, MissionActivite missionActivite) {
        uiModel.addAttribute("missionActivite", missionActivite);
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
