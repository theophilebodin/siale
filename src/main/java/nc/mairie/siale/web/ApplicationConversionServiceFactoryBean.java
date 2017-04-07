package nc.mairie.siale.web;

import nc.mairie.siale.domain.Bareme;
import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Droit;
import nc.mairie.siale.domain.Etablissement;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.MissionAction;
import nc.mairie.siale.domain.MissionActivite;
import nc.mairie.siale.domain.MissionDocument;
import nc.mairie.siale.domain.Notation;
import nc.mairie.siale.domain.NoteCritere;
import nc.mairie.siale.domain.NoteGroupe;
import nc.mairie.siale.domain.Param;
import nc.mairie.siale.domain.TypeParam;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {



	public Converter<Bareme, String> getBaremeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.Bareme, java.lang.String>() {
            public String convert(Bareme bareme) {
                return new StringBuilder().append(bareme.getNom()).append(' ').append(bareme.getDateCreation()).append(' ').append(bareme.getSeuilFaible()).append(' ').append(bareme.getSeuilModere()).toString();
            }
        };
    }

	public Converter<Long, Bareme> getIdToBaremeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.Bareme>() {
            public nc.mairie.siale.domain.Bareme convert(java.lang.Long id) {
                return Bareme.findBareme(id);
            }
        };
    }

	public Converter<String, Bareme> getStringToBaremeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.Bareme>() {
            public nc.mairie.siale.domain.Bareme convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Bareme.class);
            }
        };
    }

	public Converter<ControleurSIALE, String> getControleurSIALEToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.ControleurSIALE, java.lang.String>() {
            public String convert(ControleurSIALE controleurSIALE) {
                return new StringBuilder().append(controleurSIALE.getDisplayname()).append(' ').append(controleurSIALE.getUsername()).toString();
            }
        };
    }

	public Converter<Long, ControleurSIALE> getIdToControleurSIALEConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.ControleurSIALE>() {
            public nc.mairie.siale.domain.ControleurSIALE convert(java.lang.Long id) {
                return ControleurSIALE.findControleurSIALE(id);
            }
        };
    }

	public Converter<String, ControleurSIALE> getStringToControleurSIALEConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.ControleurSIALE>() {
            public nc.mairie.siale.domain.ControleurSIALE convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ControleurSIALE.class);
            }
        };
    }

	public Converter<Droit, String> getDroitToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.Droit, java.lang.String>() {
            public String convert(Droit droit) {
                return new StringBuilder().append(droit.getNom()).toString();
            }
        };
    }

	public Converter<Long, Droit> getIdToDroitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.Droit>() {
            public nc.mairie.siale.domain.Droit convert(java.lang.Long id) {
                return Droit.findDroit(id);
            }
        };
    }

	public Converter<String, Droit> getStringToDroitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.Droit>() {
            public nc.mairie.siale.domain.Droit convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Droit.class);
            }
        };
    }

	public Converter<Etablissement, String> getEtablissementToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.Etablissement, java.lang.String>() {
            public String convert(Etablissement etablissement) {
                return new StringBuilder().append(etablissement.getCode()).append(' ').append(etablissement.getLibelle()).append(' ').append(etablissement.getContact()).append(' ').append(etablissement.getAdresse()).toString();
            }
        };
    }

	public Converter<Long, Etablissement> getIdToEtablissementConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.Etablissement>() {
            public nc.mairie.siale.domain.Etablissement convert(java.lang.Long id) {
                return Etablissement.findEtablissement(id);
            }
        };
    }

	public Converter<String, Etablissement> getStringToEtablissementConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.Etablissement>() {
            public nc.mairie.siale.domain.Etablissement convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Etablissement.class);
            }
        };
    }

	public Converter<Mission, String> getMissionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.Mission, java.lang.String>() {
            public String convert(Mission mission) {
                return new StringBuilder().append(mission.getProjet()).append(' ').append(mission.getEtablissementNonDeclare()).append(' ').append(mission.getPrelevement_nb()).append(' ').append(mission.getPrelevement_satisfaisant()).toString();
            }
        };
    }

	public Converter<Long, Mission> getIdToMissionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.Mission>() {
            public nc.mairie.siale.domain.Mission convert(java.lang.Long id) {
                return Mission.findMission(id);
            }
        };
    }

	public Converter<String, Mission> getStringToMissionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.Mission>() {
            public nc.mairie.siale.domain.Mission convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Mission.class);
            }
        };
    }

	public Converter<MissionAction, String> getMissionActionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.MissionAction, java.lang.String>() {
            public String convert(MissionAction missionAction) {
                return new StringBuilder().append(missionAction.getDateAction()).toString();
            }
        };
    }

	public Converter<Long, MissionAction> getIdToMissionActionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.MissionAction>() {
            public nc.mairie.siale.domain.MissionAction convert(java.lang.Long id) {
                return MissionAction.findMissionAction(id);
            }
        };
    }

	public Converter<String, MissionAction> getStringToMissionActionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.MissionAction>() {
            public nc.mairie.siale.domain.MissionAction convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MissionAction.class);
            }
        };
    }

	public Converter<MissionActivite, String> getMissionActiviteToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.MissionActivite, java.lang.String>() {
            public String convert(MissionActivite missionActivite) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, MissionActivite> getIdToMissionActiviteConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.MissionActivite>() {
            public nc.mairie.siale.domain.MissionActivite convert(java.lang.Long id) {
                return MissionActivite.findMissionActivite(id);
            }
        };
    }

	public Converter<String, MissionActivite> getStringToMissionActiviteConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.MissionActivite>() {
            public nc.mairie.siale.domain.MissionActivite convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MissionActivite.class);
            }
        };
    }

	public Converter<MissionDocument, String> getMissionDocumentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.MissionDocument, java.lang.String>() {
            public String convert(MissionDocument missionDocument) {
                return new StringBuilder().append(missionDocument.getDateDocument()).toString();
            }
        };
    }

	public Converter<Long, MissionDocument> getIdToMissionDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.MissionDocument>() {
            public nc.mairie.siale.domain.MissionDocument convert(java.lang.Long id) {
                return MissionDocument.findMissionDocument(id);
            }
        };
    }

	public Converter<String, MissionDocument> getStringToMissionDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.MissionDocument>() {
            public nc.mairie.siale.domain.MissionDocument convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MissionDocument.class);
            }
        };
    }

	public Converter<Notation, String> getNotationToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.Notation, java.lang.String>() {
            public String convert(Notation notation) {
                return new StringBuilder().append(notation.getNote()).toString();
            }
        };
    }

	public Converter<Long, Notation> getIdToNotationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.Notation>() {
            public nc.mairie.siale.domain.Notation convert(java.lang.Long id) {
                return Notation.findNotation(id);
            }
        };
    }

	public Converter<String, Notation> getStringToNotationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.Notation>() {
            public nc.mairie.siale.domain.Notation convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Notation.class);
            }
        };
    }

	public Converter<NoteCritere, String> getNoteCritereToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.NoteCritere, java.lang.String>() {
            public String convert(NoteCritere noteCritere) {
                return new StringBuilder().append(noteCritere.getNom()).append(' ').append(noteCritere.getPonderation()).toString();
            }
        };
    }

	public Converter<Long, NoteCritere> getIdToNoteCritereConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.NoteCritere>() {
            public nc.mairie.siale.domain.NoteCritere convert(java.lang.Long id) {
                return NoteCritere.findNoteCritere(id);
            }
        };
    }

	public Converter<String, NoteCritere> getStringToNoteCritereConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.NoteCritere>() {
            public nc.mairie.siale.domain.NoteCritere convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), NoteCritere.class);
            }
        };
    }

	public Converter<NoteGroupe, String> getNoteGroupeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.NoteGroupe, java.lang.String>() {
            public String convert(NoteGroupe noteGroupe) {
                return new StringBuilder().append(noteGroupe.getNom()).append(' ').append(noteGroupe.getPonderation()).toString();
            }
        };
    }

	public Converter<Long, NoteGroupe> getIdToNoteGroupeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.NoteGroupe>() {
            public nc.mairie.siale.domain.NoteGroupe convert(java.lang.Long id) {
                return NoteGroupe.findNoteGroupe(id);
            }
        };
    }

	public Converter<String, NoteGroupe> getStringToNoteGroupeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.NoteGroupe>() {
            public nc.mairie.siale.domain.NoteGroupe convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), NoteGroupe.class);
            }
        };
    }

	public Converter<Param, String> getParamToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.Param, java.lang.String>() {
            public String convert(Param param) {
                return new StringBuilder().append(param.getNom()).toString();
            }
        };
    }

	public Converter<Long, Param> getIdToParamConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.Param>() {
            public nc.mairie.siale.domain.Param convert(java.lang.Long id) {
                return Param.findParam(id);
            }
        };
    }

	public Converter<String, Param> getStringToParamConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.Param>() {
            public nc.mairie.siale.domain.Param convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Param.class);
            }
        };
    }

	public Converter<TypeParam, String> getTypeParamToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<nc.mairie.siale.domain.TypeParam, java.lang.String>() {
            public String convert(TypeParam typeParam) {
                return new StringBuilder().append(typeParam.getNom()).toString();
            }
        };
    }

	public Converter<Long, TypeParam> getIdToTypeParamConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, nc.mairie.siale.domain.TypeParam>() {
            public nc.mairie.siale.domain.TypeParam convert(java.lang.Long id) {
                return TypeParam.findTypeParam(id);
            }
        };
    }

	public Converter<String, TypeParam> getStringToTypeParamConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, nc.mairie.siale.domain.TypeParam>() {
            public nc.mairie.siale.domain.TypeParam convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TypeParam.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getBaremeToStringConverter());
        registry.addConverter(getIdToBaremeConverter());
        registry.addConverter(getStringToBaremeConverter());
        registry.addConverter(getControleurSIALEToStringConverter());
        registry.addConverter(getIdToControleurSIALEConverter());
        registry.addConverter(getStringToControleurSIALEConverter());
        registry.addConverter(getDroitToStringConverter());
        registry.addConverter(getIdToDroitConverter());
        registry.addConverter(getStringToDroitConverter());
        registry.addConverter(getEtablissementToStringConverter());
        registry.addConverter(getIdToEtablissementConverter());
        registry.addConverter(getStringToEtablissementConverter());
        registry.addConverter(getMissionToStringConverter());
        registry.addConverter(getIdToMissionConverter());
        registry.addConverter(getStringToMissionConverter());
        registry.addConverter(getMissionActionToStringConverter());
        registry.addConverter(getIdToMissionActionConverter());
        registry.addConverter(getStringToMissionActionConverter());
        registry.addConverter(getMissionActiviteToStringConverter());
        registry.addConverter(getIdToMissionActiviteConverter());
        registry.addConverter(getStringToMissionActiviteConverter());
        registry.addConverter(getMissionDocumentToStringConverter());
        registry.addConverter(getIdToMissionDocumentConverter());
        registry.addConverter(getStringToMissionDocumentConverter());
        registry.addConverter(getNotationToStringConverter());
        registry.addConverter(getIdToNotationConverter());
        registry.addConverter(getStringToNotationConverter());
        registry.addConverter(getNoteCritereToStringConverter());
        registry.addConverter(getIdToNoteCritereConverter());
        registry.addConverter(getStringToNoteCritereConverter());
        registry.addConverter(getNoteGroupeToStringConverter());
        registry.addConverter(getIdToNoteGroupeConverter());
        registry.addConverter(getStringToNoteGroupeConverter());
        registry.addConverter(getParamToStringConverter());
        registry.addConverter(getIdToParamConverter());
        registry.addConverter(getStringToParamConverter());
        registry.addConverter(getTypeParamToStringConverter());
        registry.addConverter(getIdToTypeParamConverter());
        registry.addConverter(getStringToTypeParamConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
