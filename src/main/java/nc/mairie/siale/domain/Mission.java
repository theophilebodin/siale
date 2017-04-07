package nc.mairie.siale.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import nc.mairie.siale.technique.ControleSaisie;
import nc.mairie.siale.technique.RisqueEtablissement;
import nc.mairie.siale.technique.TypeEtablissement;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.zk.ui.Component;

@Entity
@Configurable
public class Mission implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3441373481810628122L;

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
                break;
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
                return getEtablissement().getNomAffichage();
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
    
    public void controleCloturable (ControleSaisie controleSaisie, Component comp) {
    	
		//controle rg_cloturee (7.1.1.5) s'il y a une date d'action
		MissionAction mc = getMissionAction(); 
		if (mc == null || mc.getDateAction() == null) {
			controleSaisie.ajouteErreur(comp,
				"La date de l'action n'a pas été saisie, la mission ne peut être cloturée");
		}
	
		//controle Fonctionnalité #3097
		int sommePrelevements = getPrelevement_satisfaisant()+getPrelevement_mediocre()+getPrelevement_non_satisfaisant();
		if (sommePrelevements != getPrelevement_nb()) {
			controleSaisie.ajouteErreur(comp,
				"Le nombre de prélèvement de la mission ne correspond pas à la somme des prélèvements S, NS et M; la mission ne peut être cloturée");
		}
		
	}
    

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "typeEtablissement", "projet", "etablissementNonDeclare", "etablissement", "prelevement_nb", "prelevement_satisfaisant", "prelevement_mediocre", "prelevement_non_satisfaisant", "missionActivites", "controleurs", "missionDocuments", "suiteDonnee", "missionAction", "dateIntervention", "datePrevue", "observation", "cloturee", "controleursSIALE", "dureePrevueRDV", "notations", "bareme", "dateNotation", "risqueEtablissement", "noteEtablissement");

	public static final EntityManager entityManager() {
        EntityManager em = new Mission().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMissions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Mission o", Long.class).getSingleResult();
    }

	public static List<Mission> findAllMissions() {
        return entityManager().createQuery("SELECT o FROM Mission o", Mission.class).getResultList();
    }

	public static List<Mission> findAllMissions(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Mission o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Mission.class).getResultList();
    }

	public static Mission findMission(Long id) {
        if (id == null) return null;
        return entityManager().find(Mission.class, id);
    }

	public static List<Mission> findMissionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Mission o", Mission.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Mission> findMissionEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Mission o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Mission.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Mission attached = Mission.findMission(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Mission merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Mission merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @SequenceGenerator(name = "missionGen", sequenceName = "mission_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "missionGen")
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public static Long countFindMissionsByClotureeNotOrDatePrevueGreaterThan(Boolean cloturee, Date datePrevue) {
        if (cloturee == null) throw new IllegalArgumentException("The cloturee argument is required");
        if (datePrevue == null) throw new IllegalArgumentException("The datePrevue argument is required");
        EntityManager em = Mission.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Mission AS o WHERE o.cloturee IS NOT :cloturee  OR o.datePrevue > :datePrevue", Long.class);
        q.setParameter("cloturee", cloturee);
        q.setParameter("datePrevue", datePrevue);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindMissionsByControleursSIALE(Set<ControleurSIALE> controleursSIALE) {
        if (controleursSIALE == null) throw new IllegalArgumentException("The controleursSIALE argument is required");
        EntityManager em = Mission.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(o) FROM Mission AS o WHERE");
        for (int i = 0; i < controleursSIALE.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :controleursSIALE_item").append(i).append(" MEMBER OF o.controleursSIALE");
        }
        TypedQuery q = em.createQuery(queryBuilder.toString(), Long.class);
        int controleursSIALEIndex = 0;
        for (ControleurSIALE _controleursiale: controleursSIALE) {
            q.setParameter("controleursSIALE_item" + controleursSIALEIndex++, _controleursiale);
        }
        return ((Long) q.getSingleResult());
    }

	public static Long countFindMissionsByControleursSIALEAndClotureeNotOrDatePrevueGreaterThan(Set<ControleurSIALE> controleursSIALE, Boolean cloturee, Date datePrevue) {
        if (controleursSIALE == null) throw new IllegalArgumentException("The controleursSIALE argument is required");
        if (cloturee == null) throw new IllegalArgumentException("The cloturee argument is required");
        if (datePrevue == null) throw new IllegalArgumentException("The datePrevue argument is required");
        EntityManager em = Mission.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(o) FROM Mission AS o WHERE o.cloturee IS NOT :cloturee  OR o.datePrevue > :datePrevue");
        queryBuilder.append(" AND");
        for (int i = 0; i < controleursSIALE.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :controleursSIALE_item").append(i).append(" MEMBER OF o.controleursSIALE");
        }
        TypedQuery q = em.createQuery(queryBuilder.toString(), Long.class);
        int controleursSIALEIndex = 0;
        for (ControleurSIALE _controleursiale: controleursSIALE) {
            q.setParameter("controleursSIALE_item" + controleursSIALEIndex++, _controleursiale);
        }
        q.setParameter("cloturee", cloturee);
        q.setParameter("datePrevue", datePrevue);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<Mission> findMissionsByClotureeNotOrDatePrevueGreaterThan(Boolean cloturee, Date datePrevue) {
        if (cloturee == null) throw new IllegalArgumentException("The cloturee argument is required");
        if (datePrevue == null) throw new IllegalArgumentException("The datePrevue argument is required");
        EntityManager em = Mission.entityManager();
        TypedQuery<Mission> q = em.createQuery("SELECT o FROM Mission AS o WHERE o.cloturee IS NOT :cloturee  OR o.datePrevue > :datePrevue", Mission.class);
        q.setParameter("cloturee", cloturee);
        q.setParameter("datePrevue", datePrevue);
        return q;
    }

	public static TypedQuery<Mission> findMissionsByClotureeNotOrDatePrevueGreaterThan(Boolean cloturee, Date datePrevue, String sortFieldName, String sortOrder) {
        if (cloturee == null) throw new IllegalArgumentException("The cloturee argument is required");
        if (datePrevue == null) throw new IllegalArgumentException("The datePrevue argument is required");
        EntityManager em = Mission.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Mission AS o WHERE o.cloturee IS NOT :cloturee  OR o.datePrevue > :datePrevue");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Mission> q = em.createQuery(queryBuilder.toString(), Mission.class);
        q.setParameter("cloturee", cloturee);
        q.setParameter("datePrevue", datePrevue);
        return q;
    }

	public static TypedQuery<Mission> findMissionsByControleursSIALE(Set<ControleurSIALE> controleursSIALE) {
        if (controleursSIALE == null) throw new IllegalArgumentException("The controleursSIALE argument is required");
        EntityManager em = Mission.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Mission AS o WHERE");
        for (int i = 0; i < controleursSIALE.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :controleursSIALE_item").append(i).append(" MEMBER OF o.controleursSIALE");
        }
        TypedQuery<Mission> q = em.createQuery(queryBuilder.toString(), Mission.class);
        int controleursSIALEIndex = 0;
        for (ControleurSIALE _controleursiale: controleursSIALE) {
            q.setParameter("controleursSIALE_item" + controleursSIALEIndex++, _controleursiale);
        }
        return q;
    }

	public static TypedQuery<Mission> findMissionsByControleursSIALE(Set<ControleurSIALE> controleursSIALE, String sortFieldName, String sortOrder) {
        if (controleursSIALE == null) throw new IllegalArgumentException("The controleursSIALE argument is required");
        EntityManager em = Mission.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Mission AS o WHERE");
        for (int i = 0; i < controleursSIALE.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :controleursSIALE_item").append(i).append(" MEMBER OF o.controleursSIALE");
        }
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" " + sortOrder);
            }
        }
        TypedQuery<Mission> q = em.createQuery(queryBuilder.toString(), Mission.class);
        int controleursSIALEIndex = 0;
        for (ControleurSIALE _controleursiale: controleursSIALE) {
            q.setParameter("controleursSIALE_item" + controleursSIALEIndex++, _controleursiale);
        }
        return q;
    }

	public static TypedQuery<Mission> findMissionsByControleursSIALEAndClotureeNotOrDatePrevueGreaterThan(Set<ControleurSIALE> controleursSIALE, Boolean cloturee, Date datePrevue, String sortFieldName, String sortOrder) {
        if (controleursSIALE == null) throw new IllegalArgumentException("The controleursSIALE argument is required");
        if (cloturee == null) throw new IllegalArgumentException("The cloturee argument is required");
        if (datePrevue == null) throw new IllegalArgumentException("The datePrevue argument is required");
        EntityManager em = Mission.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Mission AS o WHERE o.cloturee IS NOT :cloturee  OR o.datePrevue > :datePrevue");
        queryBuilder.append(" AND");
        for (int i = 0; i < controleursSIALE.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :controleursSIALE_item").append(i).append(" MEMBER OF o.controleursSIALE");
        }
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" " + sortOrder);
            }
        }
        TypedQuery<Mission> q = em.createQuery(queryBuilder.toString(), Mission.class);
        int controleursSIALEIndex = 0;
        for (ControleurSIALE _controleursiale: controleursSIALE) {
            q.setParameter("controleursSIALE_item" + controleursSIALEIndex++, _controleursiale);
        }
        q.setParameter("cloturee", cloturee);
        q.setParameter("datePrevue", datePrevue);
        return q;
    }

	public String getProjet() {
        return this.projet;
    }

	public void setProjet(String projet) {
        this.projet = projet;
    }

	public String getEtablissementNonDeclare() {
        return this.etablissementNonDeclare;
    }

	public void setEtablissementNonDeclare(String etablissementNonDeclare) {
        this.etablissementNonDeclare = etablissementNonDeclare;
    }

	public Etablissement getEtablissement() {
        return this.etablissement;
    }

	public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

	public int getPrelevement_nb() {
        return this.prelevement_nb;
    }

	public void setPrelevement_nb(int prelevement_nb) {
        this.prelevement_nb = prelevement_nb;
    }

	public int getPrelevement_satisfaisant() {
        return this.prelevement_satisfaisant;
    }

	public void setPrelevement_satisfaisant(int prelevement_satisfaisant) {
        this.prelevement_satisfaisant = prelevement_satisfaisant;
    }

	public int getPrelevement_mediocre() {
        return this.prelevement_mediocre;
    }

	public void setPrelevement_mediocre(int prelevement_mediocre) {
        this.prelevement_mediocre = prelevement_mediocre;
    }

	public int getPrelevement_non_satisfaisant() {
        return this.prelevement_non_satisfaisant;
    }

	public void setPrelevement_non_satisfaisant(int prelevement_non_satisfaisant) {
        this.prelevement_non_satisfaisant = prelevement_non_satisfaisant;
    }

	public Set<MissionActivite> getMissionActivites() {
        return this.missionActivites;
    }

	public void setMissionActivites(Set<MissionActivite> missionActivites) {
        this.missionActivites = missionActivites;
    }

	public Set<Param> getControleurs() {
        return this.controleurs;
    }

	public void setControleurs(Set<Param> controleurs) {
        this.controleurs = controleurs;
    }

	public Set<MissionDocument> getMissionDocuments() {
        return this.missionDocuments;
    }

	public void setMissionDocuments(Set<MissionDocument> missionDocuments) {
        this.missionDocuments = missionDocuments;
    }

	public Param getSuiteDonnee() {
        return this.suiteDonnee;
    }

	public void setSuiteDonnee(Param suiteDonnee) {
        this.suiteDonnee = suiteDonnee;
    }

	public MissionAction getMissionAction() {
        return this.missionAction;
    }

	public void setMissionAction(MissionAction missionAction) {
        this.missionAction = missionAction;
    }

	public Date getDateIntervention() {
        return this.dateIntervention;
    }

	public void setDateIntervention(Date dateIntervention) {
        this.dateIntervention = dateIntervention;
    }

	public Date getDatePrevue() {
        return this.datePrevue;
    }

	public void setDatePrevue(Date datePrevue) {
        this.datePrevue = datePrevue;
    }

	public String getObservation() {
        return this.observation;
    }

	public void setObservation(String observation) {
        this.observation = observation;
    }

	public Boolean getCloturee() {
        return this.cloturee;
    }

	public void setCloturee(Boolean cloturee) {
        this.cloturee = cloturee;
    }

	public Set<ControleurSIALE> getControleursSIALE() {
        return this.controleursSIALE;
    }

	public void setControleursSIALE(Set<ControleurSIALE> controleursSIALE) {
        this.controleursSIALE = controleursSIALE;
    }

	public Date getDureePrevueRDV() {
        return this.dureePrevueRDV;
    }

	public void setDureePrevueRDV(Date dureePrevueRDV) {
        this.dureePrevueRDV = dureePrevueRDV;
    }

	public Set<Notation> getNotations() {
        return this.notations;
    }

	public void setNotations(Set<Notation> notations) {
        this.notations = notations;
    }

	public Bareme getBareme() {
        return this.bareme;
    }

	public void setBareme(Bareme bareme) {
        this.bareme = bareme;
    }

	public Date getDateNotation() {
        return this.dateNotation;
    }

	public void setDateNotation(Date dateNotation) {
        this.dateNotation = dateNotation;
    }

	public RisqueEtablissement getRisqueEtablissement() {
        return this.risqueEtablissement;
    }

	public void setRisqueEtablissement(RisqueEtablissement risqueEtablissement) {
        this.risqueEtablissement = risqueEtablissement;
    }

	public Double getNoteEtablissement() {
        return this.noteEtablissement;
    }

	public void setNoteEtablissement(Double noteEtablissement) {
        this.noteEtablissement = noteEtablissement;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
