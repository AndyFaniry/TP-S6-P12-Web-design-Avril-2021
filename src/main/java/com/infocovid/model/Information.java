package com.infocovid.model;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.infocovid.bdd.ConnectionPstg;

public class Information {
	@Value("${upload.path}")
    private String uploadPath;
	int idInformation ;
    int idCategorie;
    String image;
    String motsCle;
    String titre ;
    String information;
    LocalDateTime dates;
    public Information() {
    	
    }
	public int getIdInformation() {
		return idInformation;
	}
	public void setIdInformation(int idInformation) {
		this.idInformation = idInformation;
	}
	public int getIdCategorie() {
		return idCategorie;
	}
	public void setIdCategorie(int idCategorie) {
		this.idCategorie = idCategorie;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getMotsCle() {
		return motsCle;
	}
	public void setMotsCle(String motsCle) {
		this.motsCle = motsCle;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public LocalDateTime getDates() {
		return dates;
	}
	public void setDates(LocalDateTime dates) throws Exception {
		if(dates.isBefore(LocalDateTime.now()))throw new Exception("Date de publication invalide");
		else this.dates = dates;
	}
	public Information(int idInformation, int idCategorie, String image, String motsCle, String titre,
			String information, LocalDateTime dates) {
		super();
		this.idInformation = idInformation;
		this.idCategorie = idCategorie;
		this.image = image;
		this.motsCle = motsCle;
		this.titre = titre;
		this.information = information;
		this.dates = dates;
	}
	public static ArrayList<Information> findAll(String sql) throws Exception{
		Connection co=new ConnectionPstg().getConnection();
		PreparedStatement st = null;
		ResultSet result = null;
		ArrayList<Information> array = new ArrayList<Information>();
		try {
			st = co.prepareStatement(sql);
			result = st.executeQuery(); 
			while(result.next()) {
				int idIn=result.getInt("idInformation");
				int idCa=result.getInt("idCategorie");
				String img=result.getString("image");
				String motsCle=result.getString("motsCle");
				String titre=result.getString("titre");
				String inf=result.getString("information");
				LocalDateTime date=result.getTimestamp("dates").toLocalDateTime();
				Information info=new Information(idIn,idCa,img,motsCle,titre,inf,date);
				array.add(info);
			}
		}catch(Exception e) {
			e.getMessage();
		}finally {
			if(st!=null) st.close();
		}
		return array;
    }
	public static Information getInformatiopnById(String id) throws Exception {
		String sql="select * from information where idInformation="+id;
		ArrayList<Information> info=Information.findAll(sql);
		return info.get(0);
	}
	public void insertInformation() throws Exception {
		Connection co= new ConnectionPstg().getConnection();
		PreparedStatement st = null;
		try {
			String sql= "insert into information(idCategorie,image,motsCle,titre,information,dates) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP)";
			st = co.prepareStatement(sql);
			st.setInt(1, idCategorie);
			st.setString(2, image);
			st.setString(3, motsCle);
			st.setString(4, titre);
			st.setString(5, information);
			st.execute();
			co.commit();		
		} catch (Exception e) {
			throw e;
		} finally {
			if(st != null) st.close();
			if(co!=null) co.close();
		}
	}
	public static void deleteInformation(String id) throws Exception {
		Connection co= new ConnectionPstg().getConnection();
		int idInf= Integer.parseInt(id);
		PreparedStatement st = null;
		try {
			String sql= " delete from information where idInformation=?";
			st = co.prepareStatement(sql);
			st.setInt(1,idInf);
			st.execute();
			co.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			if(st != null) st.close();
			if(co!=null) co.close();
		}
	}
	public void upDateInformation() throws Exception {
		Connection co=new ConnectionPstg().getConnection();
		PreparedStatement st = null;
		try {
			String sql= "update information set idCategorie=?, image=?, motsCle=?,titre=?,information=?,dates=CURRENT_TIMESTAMP where idInformation=?";
			st = co.prepareStatement(sql);
			st.setInt(1, idCategorie);
			st.setString(2, image);
			st.setString(3, motsCle);
			st.setString(4, titre);
			st.setString(5, information);
			st.setInt(6, idInformation);
			st.execute();
			co.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			if(st != null) st.close();
		}
	}
	public static String toStrong(String mots,String text) {
		String[] spit=mots.split(",");
		String[] parPoint=text.split("(?=\\p{Punct})|(?<=\\p{Punct})");
		String[] strong=new String[parPoint.length];
		String valiny="";
		for(int i=0;i<parPoint.length;i++) {
			String[] parEspace=parPoint[i].split(" ");
			for(int j=0;j<spit.length;j++) {
				for(int x=0;x<parEspace.length;x++) {
					if(parEspace[x].toLowerCase().compareTo(spit[j].toLowerCase())==0) {
						parEspace[x]="<strong>"+parEspace[x]+"</strong>";
					}
				}
			}
			strong[i]=String.join(" ",parEspace);
		}
		for(int i=0;i<strong.length;i++) {
			valiny=valiny+strong[i];
		}
		return valiny;
	}
	public static void saveFile(String uploadDir, String fileName,MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);    
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }   
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        }      
    }
	public static String imagePath(String image) {
		String path="/images/article/";
		String date=LocalDateTime.now().toString().split("T")[0];
		path=path+date;
		return path; 
	}
}
