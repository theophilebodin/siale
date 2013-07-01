package nc.mairie.siale.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import nc.mairie.siale.technique.RisqueEtablissement;
import nc.mairie.siale.technique.TypeEtablissement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "mission_sequence", finders = { "findMissionsByControleursSIALE", "findMissionsByClotureeNotOrDatePrevueGreaterThan", "findMissionsByControleursSIALEAndClotureeNotOrDatePrevueGreaterThan" })
public class Mission {

    TypeEtablissement typeEtablissement;

    private String projet;

    private String etablissementNonDeclare;

    @ManyToOne
    private Etablissement etablissement;

    private int prelevement_nb;

    private int prelevement_satisfaisant;

    private int prelevement_mediocre;

    private int prelevement_non_satisfaisant;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "theMission", orphanRemoval = true, fetch= FetchType.EAGER)
    private Set<MissionActivite> missionActivites = new HashSet<MissionActivite>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Param> controleurs = new HashSet<Param>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "theMission", orphanRemoval = true)
    private Set<MissionDocument> missionDocuments = new HashSet<MissionDocument>();

    @ManyToOne
    private Param suiteDonnee;

    @OneToOne(cascade = CascadeType.ALL)
    private MissionAction missionAction;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateIntervention;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date datePrevue;

    @Size(max = 1000)
    private String observation;

    @NotNull
    @Value("false")
    private Boolean cloturee;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ControleurSIALE> controleursSIALE = new HashSet<ControleurSIALE>();

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(style = "-S")
    private Date dureePrevueRDV;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mission", fetch= FetchType.EAGER)
    private Set<Notation> notations = new HashSet<Notation>();

    @ManyToOne
    private Bareme bareme;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateNotation;

    private RisqueEtablissement risqueEtablissement;
    
    private Double noteEtablissement;
    
    public void setTypeEtablissement(TypeEtablissement typeEtablissement) {
        this.typeEtablissement = typeEtablissement;
        switch(typeEtablissement) {
            case ETABLISSEMENT:
                setProjet(null);
                setEtablissementNonDeclare(null);
                break;
            case NONDECLARE:
                setProjet(null);
                setEtablissement(null);
            case PROJET:
                setEtablissement(null);
                setEtablissementNonDeclare(null);
                break;
            default:
                break;
        }
    }

    public String getTypeEtablissementToString() {
        return getTypeEtablissement().toString();
    }

    public TypeEtablissement getTypeEtablissement() {
        if (typeEtablissement == null) {
            typeEtablissement = getEtablissement() != null ? TypeEtablissement.ETABLISSEMENT : getProjet() != null ? TypeEtablissement.PROJET : getEtablissementNonDeclare() != null ? TypeEtablissement.NONDECLARE : null;
        }
        return typeEtablissement;
    }

    public boolean isProjetSelected() {
        return TypeEtablissement.PROJET.equals(getTypeEtablissement());
    }

    public boolean isEtablissementSelected() {
        return TypeEtablissement.ETABLISSEMENT.equals(getTypeEtablissement());
    }

    public boolean isNonDeclareSelected() {
        return TypeEtablissement.NONDECLARE.equals(getTypeEtablissement());
    }

    public String getNomEtablissement() {
        if (getTypeEtablissement() == null) return null;
        switch(getTypeEtablissement()) {
            case ETABLISSEMENT:
                return getEtablissement().getLibelle();
            case NONDECLARE:
                return getEtablissementNonDeclare();
            case PROJET:
                return getProjet();
            default:
                return "incoherence données";
        }
    }

    public Param getActivitePrincipale() {
        for (MissionActivite activite : getMissionActivites()) {
            if (activite.getPrincipale()) {
                return activite.getTheActivite();
            }
        }
        return null;
    }
    
   //Retourne vrai si des notations concernent la mission
    public boolean isPossedeNotations () {
    	return getNotations().size() > 0;
    }
    
   //Retourne vrai si des saisies concernent la mission
    public boolean isPossedeSaisies () {
    	return getSuiteDonnee() != null;
    }

    public static TypedQuery<nc.mairie.siale.domain.Mission> findMissionsByControleursSIALEAndClotureeNotOrDatePrevueGreaterThan(Set<nc.mairie.siale.domain.ControleurSIALE> controleursSIALE, Boolean cloturee, Date datePrevue) {
        if (controleursSIALE == null) throw new IllegalArgumentException("The controleursSIALE argument is required");
        if (cloturee == null) throw new IllegalArgumentException("The cloturee argument is required");
        if (datePrevue == null) throw new IllegalArgumentException("The datePrevue argument is required");
        EntityManager em = Mission.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Mission AS o WHERE (o.cloturee IS NOT :cloturee  OR o.datePrevue > :datePrevue)");
        queryBuilder.append(" AND");
        for (int i = 0; i < controleursSIALE.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :controleursSIALE_item").append(i).append(" MEMBER OF o.controleursSIALE");
        }
        TypedQuery<Mission> q = em.createQuery(queryBuilder.toString(), Mission.class);
        int controleursSIALEIndex = 0;
        for (ControleurSIALE _controleursiale : controleursSIALE) {
            q.setParameter("controleursSIALE_item" + controleursSIALEIndex++, _controleursiale);
        }
        q.setParameter("cloturee", cloturee);
        q.setParameter("datePrevue", datePrevue);
        return q;
    }

    public static boolean existMissionsByBareme(Bareme bareme) {
        if (bareme == null) throw new IllegalArgumentException("The bareme argument is required");
        EntityManager em = Mission.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT count(*) FROM Mission AS o WHERE o.bareme = :bareme", Long.class);
        q.setParameter("bareme", bareme);
        return q.getResultList().get(0) != 0;
    }
    
    public static boolean existMissionsByControleurSIALE(ControleurSIALE controleurSIALE) {
        if (controleurSIALE == null) throw new IllegalArgumentException("The controleurSIALE argument is required");
        EntityManager em = Mission.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT count(*) FROM Mission AS o WHERE :controleurSIALE MEMBER OF o.controleursSIALE", Long.class);
        q.setParameter("controleurSIALE", controleurSIALE);
        return q.getResultList().get(0) != 0;
    }
    
    
}
