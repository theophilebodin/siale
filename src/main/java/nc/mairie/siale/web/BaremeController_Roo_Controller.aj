// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.Bareme;
import nc.mairie.siale.domain.NoteGroupe;
import nc.mairie.siale.web.BaremeController;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect BaremeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String BaremeController.create(@Valid Bareme bareme, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bareme);
            return "baremes/create";
        }
        uiModel.asMap().clear();
        bareme.persist();
        return "redirect:/baremes/" + encodeUrlPathSegment(bareme.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String BaremeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Bareme());
        return "baremes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String BaremeController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("bareme", Bareme.findBareme(id));
        uiModel.addAttribute("itemId", id);
        return "baremes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String BaremeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("baremes", Bareme.findBaremeEntries(firstResult, sizeNo));
            float nrOfPages = (float) Bareme.countBaremes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("baremes", Bareme.findAllBaremes());
        }
        addDateTimeFormatPatterns(uiModel);
        return "baremes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String BaremeController.update(@Valid Bareme bareme, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bareme);
            return "baremes/update";
        }
        uiModel.asMap().clear();
        bareme.merge();
        return "redirect:/baremes/" + encodeUrlPathSegment(bareme.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String BaremeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Bareme.findBareme(id));
        return "baremes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String BaremeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Bareme bareme = Bareme.findBareme(id);
        bareme.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/baremes";
    }
    
    void BaremeController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("bareme_datecreation_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void BaremeController.populateEditForm(Model uiModel, Bareme bareme) {
        uiModel.addAttribute("bareme", bareme);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("notegroupes", NoteGroupe.findAllNoteGroupes());
    }
    
    String BaremeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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