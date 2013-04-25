package nc.mairie.siale.technique;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.UUID;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.zkoss.zk.ui.Executions;

import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Mission;

public class Outlook {

	//Récupérés du contexte
	public static String host;
	public static String from;
	
	public static void sendCalendar(Mission mission) throws Exception{
		String subject =  "Rendez-vous pour une mission SIALE";
		String messageMail = "Double cliquer sur le fichier joint ou glissez-le dans le calendrier d'Outlook.\n" +
							 "Rendez-vous avec "+mission.getNomEtablissement();

		Hashtable<String, String> destinataires = new Hashtable<String, String>();
		
		for (ControleurSIALE controleurSIALE : mission.getControleursSIALE()) {
			destinataires.put(controleurSIALE.getNomAffichage(), controleurSIALE.getUsername()+"@ville-noumea.nc");
		}
		
		sendCalendar(subject,messageMail,destinataires, mission.getDatePrevue(), mission.getDureePrevueRDV(),mission.getNomEtablissement(), "Mission SIALE");
	}
	
	/**
	 * 
	 * @param subject
	 * @param messageMail
	 * @param destinataires hashtable avec en clé le nom d'usage et en valeur l'@ mail
	 * @param datePrevue
	 * @param duree
	 * @param lieu
	 * @param filename
	 */
	public static void sendCalendar(String subject, 
									String messageMail, 
									Hashtable<String, String> destinataires, 
									Date datePrevue, 
									Date duree, 
									String lieu,
									String filename) throws Exception{
	//	try {
		
			host = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("HOST_SMTP");
			if (host == null) throw new Exception("Impossible de récupérer le paramètre HOST_SMTP");
			from = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("MAIL_SENDER");
			if (from == null) throw new Exception("Impossible de récupérer le paramètre MAIL_SENDER");
			
			// Get system properties
			Properties props = System.getProperties();

			// Setup mail server
			props.put("mail.smtp.host", host);

			// Get session
			Session session = Session.getInstance(props, null);

			// Define message
			MimeMessage message = new MimeMessage(session);
			message.addHeaderLine("method=REQUEST");
			message.addHeaderLine("charset=UTF-8");
			message.addHeaderLine("component=vevent");
			message.setFrom(new InternetAddress(from));
			
			Enumeration<String> enume = destinataires.keys();
			while (enume.hasMoreElements()) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinataires.get(enume.nextElement())));
			}
			
			message.setSubject(subject);

			StringBuffer sb = new StringBuffer();

			StringBuffer buffer = sb
					.append("BEGIN:VCALENDAR\n"
							+ "PRODID:-//Microsoft Corporation//Outlook 11.0 MIMEDIR//EN\n"
							+ "VERSION:2.0\n"
							+ "METHOD:REQUEST\n"
							+ "BEGIN:VEVENT\n");
			
			enume = destinataires.keys();
			while (enume.hasMoreElements()) {
				String nom = enume.nextElement();
				String mail = destinataires.get(nom);
				buffer = buffer.append("ATTENDEE;CN=\""+nom+"\";ROLE=REQ-PARTICIPANT;MAILTO:"+mail+"\n");
				//buffer = buffer.append("ATTENDEE;ROLE=REQ-PARTICIPANT;MAILTO:"+destinataire+"\n");
			}
			
			
			//DTSTART : 20130406T073000Z
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String DTSTART = sdf.format(datePrevue);
			DTSTART=DTSTART.substring(0,8)+"T"+DTSTART.substring(8,14);

			//DTEND : 20130406T080000Z
			Calendar cal=Calendar.getInstance();
			cal.setTime(duree);
			int heures = cal.get(Calendar.HOUR);
			int minutes = cal.get(Calendar.MINUTE);
			
			cal.setTime(datePrevue);
			cal.add(Calendar.HOUR, heures);
			cal.add(Calendar.MINUTE, minutes);
			String DTEND = sdf.format(cal.getTime());
			DTEND=DTEND.substring(0,8)+"T"+DTEND.substring(8,14);

			//DTSTAMP
			String DTSTAMP = sdf.format(new Date());
			DTSTAMP=DTSTAMP.substring(0,8)+"T"+DTSTAMP.substring(8,14);

			
//					"ATTENDEE;CN=\"Tartenpion TOTO)\";ROLE=OPT-PARTICIPANT;MAILTO:toto@toto.com\n"
			buffer = buffer.append(  "ORGANIZER:MAILTO:"+from+"\n"
							+ "DTSTART:"+DTSTART+"\n"
							+ "DTEND:"+DTEND+"\n"
							+ "LOCATION:"+lieu+"\n"
							+ "TRANSP:OPAQUE\n"
							+ "SEQUENCE:0\n"
							//+ "UID:040000008200E00074C5B7101A82E00800000000A0A742E5073AC5010000000000000000100\n"
							+ "UID:"+UUID.randomUUID()+"\n"
							+ " 0000029606C073D82204AB6C77ACE6BC2FBE4\n"
							+ "DTSTAMP:"+DTSTAMP+"\n"
							+ "CATEGORIES:Rendez-vous\n"
							+ "DESCRIPTION:"+messageMail+"\n\n"
							+ "SUMMARY:"+subject+"\n" + "PRIORITY:5\n"
							+ "CLASS:PUBLIC\n" + "BEGIN:VALARM\n"
							+ "TRIGGER:PT1440M\n" + "ACTION:DISPLAY\n"
							+ "DESCRIPTION:Rappel\n" + "END:VALARM\n"
							+ "END:VEVENT\n" + "END:VCALENDAR\n");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
			messageBodyPart.setHeader("Content-ID", "calendar_message");
			messageBodyPart.setContent(buffer.toString(), "text/calendar");
			// Fill the message
			messageBodyPart
					.setText(messageMail);

			// Create a Multipart
			Multipart multipart = new MimeMultipart();

			// Add part one
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			// Create second body part
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setFileName(filename.toUpperCase().endsWith(".ICS") ? filename : filename+".ics");
			messageBodyPart.setContent(buffer.toString(), "text/plain");

			// Add part two
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			message.setContent(multipart);

			// send message
			Transport.send(message);

	}


	public static void sendSIALECalenndarOLD (Mission mission) {
		try {
			//A recup du contexte
			String host = "smtp.site-mairie.noumea.nc";
			String from = "SIALE-no-reply@ville-noumea.nc";
			
			
			//A passer en parametres
					String subject =  "Rendez-vous pour une mission SIALE";
			String messageMail = "Double cliquer sur le fichier joint ou glissez-le dans le calendrier d'Outlook.\n" +
								 "Rendez-vous avec "+mission.getNomEtablissement();
			
			// Get system properties
			Properties props = System.getProperties();

			// Setup mail server
			props.put("mail.smtp.host", host);

			// Get session
			Session session = Session.getInstance(props, null);

			// Define message
			MimeMessage message = new MimeMessage(session);
			message.addHeaderLine("method=REQUEST");
			message.addHeaderLine("charset=UTF-8");
			message.addHeaderLine("component=vevent");
			message.setFrom(new InternetAddress(from));
			
			for (ControleurSIALE controleurSIALE : mission.getControleursSIALE()) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(controleurSIALE.getUsername()+"@ville-noumea.nc"));
			}
			
			message.setSubject(subject);

			StringBuffer sb = new StringBuffer();

			StringBuffer buffer = sb
					.append("BEGIN:VCALENDAR\n"
							+ "PRODID:-//Microsoft Corporation//Outlook 11.0 MIMEDIR//EN\n"
							+ "VERSION:2.0\n"
							+ "METHOD:REQUEST\n"
							+ "BEGIN:VEVENT\n");
			
			for (ControleurSIALE controleurSIALE : mission.getControleursSIALE()) {
				buffer = buffer.append("ATTENDEE;CN=\""+controleurSIALE.getNomAffichage()+"\";ROLE=REQ-PARTICIPANT;MAILTO:"+controleurSIALE.getUsername()+"@ville-noumea.nc\n");
			}
			
			
			//DTSTART : 20130406T073000Z
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String DTSTART = sdf.format(mission.getDatePrevue());
			DTSTART=DTSTART.substring(0,8)+"T"+DTSTART.substring(8,14);

			//DTEND : 20130406T080000Z
			Calendar cal=Calendar.getInstance();
			cal.setTime(mission.getDureePrevueRDV());
			int heures = cal.get(Calendar.HOUR);
			int minutes = cal.get(Calendar.MINUTE);
			
			cal.setTime(mission.getDatePrevue());
			cal.add(Calendar.HOUR, heures);
			cal.add(Calendar.MINUTE, minutes);
			String DTEND = sdf.format(cal.getTime());
			DTEND=DTEND.substring(0,8)+"T"+DTEND.substring(8,14);
			
			//DTSTAMP
			String DTSTAMP = sdf.format(new Date());
			DTSTAMP=DTSTAMP.substring(0,8)+"T"+DTSTAMP.substring(8,14);

			
//					"ATTENDEE;CN=\"Tartenpion TOTO)\";ROLE=OPT-PARTICIPANT;MAILTO:toto@toto.com\n"
			buffer = buffer.append(  "ORGANIZER:MAILTO:"+from+"\n"
							+ "DTSTART:"+DTSTART+"\n"
							+ "DTEND:"+DTEND+"\n"
							+ "LOCATION:"+mission.getNomEtablissement()+"\n"
							+ "TRANSP:OPAQUE\n"
							+ "SEQUENCE:0\n"
							//+ "UID:040000008200E00074C5B7101A82E00800000000A0A742E5073AC5010000000000000000100\n"
							+ "UID:"+UUID.randomUUID()+"\n"
							+ " 0000029606C073D82204AB6C77ACE6BC2FBE4\n"
							+ "DTSTAMP:"+DTSTAMP+"\n"
							+ "CATEGORIES:Rendez-vous\n"
							+ "DESCRIPTION:"+messageMail+"\n\n"
							+ "SUMMARY:"+subject+"\n" + "PRIORITY:5\n"
							+ "CLASS:PUBLIC\n" + "BEGIN:VALARM\n"
							+ "TRIGGER:PT1440M\n" + "ACTION:DISPLAY\n"
							+ "DESCRIPTION:Rappel\n" + "END:VALARM\n"
							+ "END:VEVENT\n" + "END:VCALENDAR\n");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
			messageBodyPart.setHeader("Content-ID", "calendar_message");
			messageBodyPart.setContent(buffer.toString(), "text/calendar");
			// Fill the message
			messageBodyPart
					.setText(messageMail);

			// Create a Multipart
			Multipart multipart = new MimeMultipart();

			// Add part one
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			// Create second body part
			messageBodyPart = new MimeBodyPart();
			String filename = "Mission SIALE.ics";
			messageBodyPart.setFileName(filename);
			messageBodyPart.setContent(buffer.toString(), "text/plain");

			// Add part two
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			message.setContent(multipart);

			// send message
			Transport.send(message);
		} catch (MessagingException me) {
			me.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
