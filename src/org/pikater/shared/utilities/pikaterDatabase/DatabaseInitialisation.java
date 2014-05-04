package org.pikater.shared.utilities.pikaterDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.postgresql.PGConnection;
import org.pikater.core.agents.system.Agent_DataManager;
import org.pikater.shared.database.jpa.JPAExperimentStatus;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.JPAUserPriviledge;
import org.pikater.shared.database.PostgreSQLConnectionProvider;
import org.pikater.shared.utilities.pikaterDatabase.daos.DAOs;
import org.pikater.shared.utilities.pikaterDatabase.exceptions.UserNotFoundException;
import org.pikater.shared.utilities.pikaterDatabase.initialisation.JPAMetaDataReader;

public class DatabaseInitialisation {

	public enum PasswordChangeResult{Success,Error};
	
	PGConnection connection;
	EntityManagerFactory emf=null;
	EntityManager em = null;
	Database database = null;
	
	public DatabaseInitialisation(EntityManagerFactory emf,PGConnection connection){
		this.emf=emf;
		this.connection=connection;
		this.database = new Database(emf, connection);
	}

	
	/**
	 * Initialisation of test-data in DataBase
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws UserNotFoundException 
	 * @throws ParseException 
	 */
	private void itialisationData() throws SQLException, IOException, UserNotFoundException, ParseException{				
		
		this.createRolesAndUsers();
		
		//this.addWebDatasets();
		
		/**

		this.insertFinishedBatch();
		
		this.createFileMapping();
		**/
	}
	
	private void addWebDatasets() throws FileNotFoundException, IOException, UserNotFoundException, SQLException{
		File dir=new File(Agent_DataManager.datasetsPath);
		JPAUser user = database.getUserByLogin("stepan");
		System.out.println("Target user: "+user.getLogin());
		
		File[] datasets=dir.listFiles();
		for(File datasetI : datasets){
			if(datasetI.isFile()){
				try{
				System.out.println("--------------------");
				System.out.println("Dataset: "+datasetI.getAbsolutePath());
				JPAMetaDataReader mdr=new JPAMetaDataReader(database);
				mdr.readFile(datasetI);		
				System.out.println("MD5 hash: "+database.getMD5Hash(datasetI));
			
				JPADataSetLO dslo=database.saveDataSet(
					user,
					datasetI,
					datasetI.getName(),
					mdr.getJPAGlobalMetaData(),
					mdr.getJPAAttributeMetaData()
					);
				System.out.println(dslo);
				System.out.println("--------------------");
				System.out.println();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	


	private void createRolesAndUsers() throws UserNotFoundException {

		database.addUserPriviledge("SaveDataSet");
		database.addUserPriviledge("SaveBox");
		
		
		database.addRole("user", "Standard User Role");
		database.addRole("admin","Admin role");
		
		database.addPriviledgeForRole("user", "SaveDataSet");
		
		database.addPriviledgeForRole("admin", "SaveDataSet");
		database.addPriviledgeForRole("admin", "SaveBox");
		
		
		database.addUser("stepan", "123", "Bc.Stepan.Balcar@gmail.com", 9);
		database.setRoleForUser("stepan", "admin");
		
		database.addUser("kj", "123", "kj@gmail.com", 9);
		database.setRoleForUser("kj", "admin");
		
		database.addUser("sj", "123", "sj@gmail.com", 9);
		database.setRoleForUser("sj", "admin");
		
		database.addUser("sp", "123", "sp@gmail.com", 9);
		database.setRoleForUser("sp", "admin");
		
		database.addUser("martin", "123", "Martin.Pilat@mff.cuni.cz", 9);
		database.setRoleForUser("martin", "user");
		
		database.addUser("klara", "123", "peskova@braille.mff.cuni.cz", 9);
		database.setRoleForUser("klara", "user");
	}
	
	private void insertFinishedBatch() throws ParseException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JPAResult result = new JPAResult();
		result.setAgentName("RBFNetwork");
		result.setAgentTypeId(0);
		result.setErrorRate(0.214285716414452);
		result.setFinish(dateFormat.parse("2014-03-29 11:06:57"));
		result.setKappaStatistic(0.511627912521362);
		result.setMeanAbsoluteError(0.264998614788055);
		result.setNote("Note of result :-)");
		result.setOptions("-S 0 -M 0.2 ");
		result.setRelativeAbsoluteError(55.6497116088867);
		result.setRootMeanSquaredError(0.462737262248993);
		result.setRootRelativeSquaredError(93.7923049926758);
		result.setSerializedFileName("");
		result.setStart(dateFormat.parse("2014-03-29 11:06:55"));
		
		JPAExperiment experiment = new JPAExperiment();
		experiment.setStatus(JPAExperimentStatus.FINISHED);
		experiment.addResult(result);

		JPABatch batch = new JPABatch();
		batch.setName("Stepan's batch of experiments - school project");
		batch.setPriority(99);
		batch.addExperiment(experiment);

		this.database.persist(batch);
	}
	
	private void createFileMapping() {
		
		JPAFilemapping f = new JPAFilemapping();
		f.setUser(DAOs.userDAO.getByLogin("stepan").get(0));
		f.setExternalfilename("iris.arff");
		f.setInternalfilename("25d7d5d689042a3816aa1598d5fd56ef");
		DAOs.filemappingDAO.storeEntity(f);
		
		JPAFilemapping f2 = new JPAFilemapping();
		f2.setUser(DAOs.userDAO.getByLogin("stepan").get(0));
		f2.setExternalfilename("weather.arff");
		f2.setInternalfilename("772c551b8486b932aed784a582b9c1b1");
		DAOs.filemappingDAO.storeEntity(f);

	}

	private void testData() throws SQLException, IOException, UserNotFoundException{
		
		database.addRole("user", "Standard user role");
		database.addRole("admin","Standard administrator role");
		
		database.addUser("stepan", "123", "bc.stepan.balcar@gmail.com", 9); // + role
		database.addUser("kj", "123", "nassoftwerak@gmail.com", 6);
		database.addUser("sj", "123", "nassoftwerak@gmail.com", 6);
		database.addUser("sp", "123", "nassoftwerak@gmail.com", 6);
		database.addUser("martin", "123", "Martin.Pilat@mff.cuni.cz", 0);

		database.setRoleForUser("stepan", "admin");
		database.setRoleForUser("kj", "admin");
		database.setRoleForUser("sj", "admin");
		database.setRoleForUser("sp", "admin");
		database.setRoleForUser("martin", "user");
				
		JPAUserPriviledge priviledgeSaveData = new JPAUserPriviledge();
		priviledgeSaveData.setName("SaveDataSet");

		JPAUserPriviledge priviledgeSaveBox = new JPAUserPriviledge();
		priviledgeSaveBox.setName("SaveBox");

		JPARole roleAdmin = database.getRoleByName("admin");
		roleAdmin.addPriviledge(priviledgeSaveData);
		roleAdmin.addPriviledge(priviledgeSaveBox);
		
		JPARole roleUser = database.getRoleByName("user");
		roleUser.addPriviledge(priviledgeSaveData);
	
		JPAUser stepan = database.getUserByLogin("stepan");
		stepan.setRole(roleAdmin);

		database.persist(stepan);
		/**
		JPAUser john=this.getUserByLogin("johndoe");
		this.saveGeneralFile(john.getId(), "First Data File",new File( "./data/files/25d7d5d689042a3816aa1598d5fd56ef"));
		this.saveGeneralFile(john.getId(), "Second Data File",new File( "./data/files/772c551b8486b932aed784a582b9c1b1"));
		this.saveGeneralFile(john.getId(), "Third Data File",new File( "./data/files/dc7ce6dea5a75110486760cfac1051a5"));
		**/
		
		
		// Test of Datasets
		for(JPADataSetLO dslo:database.getAllDataSetLargeObjects()){
			System.out.println("OID: "+dslo.getOID()+"  Hash:  "+dslo.getHash()+"  "+dslo.getDescription()+" ---  "+dslo.getOwner().getLogin()+"  GM.noInst: "+dslo.getGlobalMetaData().getNumberofInstances()+"  GM.DefTT: "+dslo.getGlobalMetaData().getDefaultTaskType().getName() );
		}

	}


	public static void main(String[] args) throws SQLException, IOException, UserNotFoundException, ClassNotFoundException, ParseException {

		EntityManagerFactory emf=Persistence.createEntityManagerFactory("pikaterDataModel");

		DatabaseInitialisation data = new DatabaseInitialisation(
				emf,(PGConnection)(
						new PostgreSQLConnectionProvider(
								"jdbc:postgresql://nassoftwerak.ms.mff.cuni.cz:5432/pikater",
								"pikater",
								"SrapRoPy").getConnection()));
		data.itialisationData();

		
		System.out.println("mm");
	}
}
