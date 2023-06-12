package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.Experience;
import com.chicmic.eNaukri.model.UserSkills;
import com.chicmic.eNaukri.model.Users;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import scala.xml.Elem;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

import static com.itextpdf.text.pdf.PdfName.T;
//import static sun.security.ssl.SSLLogger.SSLSimpleFormatter.formatObject;

//import static com.itextpdf.text.pdf.PdfName.T;
//import static sun.security.ssl.SSLLogger.SSLSimpleFormatter.formatObject;

@Service public class ResumeGenerator {
    private <T> String formatObject(T object) {
        StringBuilder builder = new StringBuilder();

        Class<?> objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object fieldValue = field.get(object);
                builder.append(field.getName()).append(": ").append(fieldValue).append("\n");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        builder.append("\n");
        return builder.toString();
    }

    public void generatePDF(HttpServletResponse response, Users user) throws IOException, DocumentException {

//        Document document = new Document();
//        PdfDocument dc=new PdfDocument();
////        PdfContentStreamProcessor processor=new PdfContentStreamProcessor()
//        // Set response headers for PDF download
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=\"example.pdf\"");
//
//        // Create PDF writer to write the document to the response's output stream
//        OutputStream outputStream = response.getOutputStream();
//        PdfWriter.getInstance(document, outputStream);
//
//        // Open the document and add content
//        document.open();
//        document.add(new Paragraph("Hello, World! \n hhfjsdgfcyugdifgcyegfgheugf"+"degree"+user.getExperienceList().get(0).)); // Add content to the PDF
//
//        // Close the document
//        document.close();
//        outputStream.close();
//    }
        try {
            // Create a new PDF document
//            PdfDocument pdfDoc = new PdfDocument(new PdfWriter("user_resume.pdf"));

            // Create a new document instance
            Document doc = new Document();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"example.pdf\"");

            // Create PDF writer to write the document to the response's output stream
            OutputStream outputStream = response.getOutputStream();
            PdfWriter.getInstance(doc, outputStream);
            doc.open();

            Font font=new Font();
            font.setFamily("Courier");

            // Add user details to the document
//            Paragraph userSection = new Paragraph();
//            userSection.setIndentationLeft(1);
//            userSection.setFont(font);
//            userSection.add("");
//            doc.add(userSection);

//            doc.add(new Paragraph("Name: " + user.getName()));
//            doc.add(new Paragraph("Email: " + user.getEmail()));
//            doc.add(new Paragraph("Phone: " + user.getPhone()));

            // Add education details to the document
//            Paragraph educationSection = new Paragraph()
//                    .setMarginTop(30)
//                    .setTextAlignment(TextAlignment.LEFT)
//                    .setFont(PdfFontFactory.createFont())
//                    .add(new Text("Education Details"))
//                    .setBold();
//            doc.add(educationSection);
//            doc.open();
            font.setFamily("Arial");
            font.setColor(128,209,190);
            Chunk textUnderline = new Chunk("Education: ", font);
            textUnderline.setUnderline(0.8f, -1f);
            doc.add(textUnderline);

            for (Education education : user.getEducationList()) {
                doc.add(new Paragraph("Institution: " + education.getUniversityName() + "Degree: " + education.getDegree()));
//                doc.add(new Paragraph();
                doc.add(new Paragraph(education.getStartFrom()+"-"+education.getEndOn()));
                doc.add(new Paragraph(education.getMajors()));
                doc.add(new Paragraph(""));
                // Add additional education details as needed
            }
            for(Experience experience: user.getExperienceList()){
                doc.add(new Paragraph("Company: " + experience.getExCompany().getCompanyName()));
//                doc.add(new Paragraph();
                doc.add(new Paragraph(experience.getJoinedOn()+"-"+experience.getEndedOn()));
                doc.add(new Paragraph(experience.getRole()+":"+experience.getRoleDesc()));
                doc.add(new Paragraph(""));
            }
            String newString="Skills: ";
            StringBuilder skillsStringBuilder = new StringBuilder();

            for (UserSkills skills : user.getUserSkillsList()) {
                skillsStringBuilder.append(skills.getSkills().getSkillName()).append(", ");
            }

            String skillsString = skillsStringBuilder.toString();
//            skillsString = skillsString.substring(0, skillsString.length() - 2); // Remove the trailing comma and space

            doc.add(new Paragraph(skillsString));
            doc.add(new Paragraph(""));
//            for(UserSkills skills: user.getUserSkillsList()){
//                doc.add(new Paragraph(skills.getSkills().getSkillName()));
//                doc.add(new Paragraph(""));
//            }

        // Open the document and add content

        doc.add(new Paragraph("Hello, World! \n hhfjsdgfcyugdifgcyegfgheugf"+"degree")); // Add content to the PDF

        // Close the document
        doc.close();
        outputStream.close();

            // Close the document

        } catch (Exception e) {
            // Handle any exceptions that occur during PDF generation
            e.printStackTrace();
        }
    }
}
