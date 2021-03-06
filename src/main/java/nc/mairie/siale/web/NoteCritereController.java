package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.NoteCritere;
import nc.mairie.siale.domain.NoteGroupe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/notecriteres")
@Controller
public class NoteCritereController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid NoteCritere noteCritere, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, noteCritere);
            return "notecriteres/create";
        }
        uiModel.asMap().clear();
        noteCritere.persist();
        return "redirect:/notecriteres/" + encodeUrlPathSegment(noteCritere.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new NoteCritere());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (NoteGroupe.countNoteGroupes() == 0) {
            dependencies.add(new String[] { "noteGroupe", "notegroupes" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "notecriteres/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("notecritere", NoteCritere.findNoteCritere(id));
        uiModel.addAttribute("itemId", id);
        return "notecriteres/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("notecriteres", NoteCritere.findNoteCritereEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) NoteCritere.countNoteCriteres() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("notecriteres", NoteCritere.findAllNoteCriteres(sortFieldName, sortOrder));
        }
        return "notecriteres/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid NoteCritere noteCritere, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, noteCritere);
            return "notecriteres/update";
        }
        uiModel.asMap().clear();
        noteCritere.merge();
        return "redirect:/notecriteres/" + encodeUrlPathSegment(noteCritere.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, NoteCritere.findNoteCritere(id));
        return "notecriteres/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        NoteCritere noteCritere = NoteCritere.findNoteCritere(id);
        noteCritere.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/notecriteres";
    }

	void populateEditForm(Model uiModel, NoteCritere noteCritere) {
        uiModel.addAttribute("noteCritere", noteCritere);
        uiModel.addAttribute("notegroupes", NoteGroupe.findAllNoteGroupes());
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
