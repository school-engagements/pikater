package org.pikater.shared.utilities.pikaterDatabase.daos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.pikater.core.agents.system.metadata.MetadataReader;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.core.ontology.messages.DataInstances;
import org.pikater.core.ontology.messages.Metadata;
import org.pikater.core.ontology.messages.metadata.AttributeMetadata;
import org.pikater.core.ontology.messages.metadata.CategoricalAttributeMetadata;
import org.pikater.core.ontology.messages.metadata.IntegerAttributeMetadata;
import org.pikater.core.ontology.messages.metadata.RealAttributeMetadata;

import weka.core.Instances;

public class NewJPAMetaDataReader {

		MetadataReader reader;
		Metadata md=null;
		
		public NewJPAMetaDataReader(){
			reader=new MetadataReader();
		}
		
		public void readFile(File file) throws FileNotFoundException, IOException{	
			DataInstances data=new DataInstances();
			data.fillWekaInstances(new Instances(new BufferedReader(new FileReader(file))));
			md=reader.computeMetadata(data);
		}
		
		public JPAGlobalMetaData getJPAGlobalMetaData(){
			JPAGlobalMetaData globMD=new JPAGlobalMetaData();
			globMD.setNumberofInstances(md.getNumber_of_instances());
			globMD.setDefaultTaskType(DAOs.taskTypeDAO.createOrGetByName(md.getDefault_task()));
			//globMD.setDefaultTaskType(db.getOrCreateTaskTypeByName((md.getDefault_task())));
			return globMD;
		}
		
		public List<JPAAttributeMetaData> getJPAAttributeMetaData(){
			
			List<JPAAttributeMetaData> attrs=new LinkedList<JPAAttributeMetaData>();
			
			for (int i=md.getAttribute_metadata_list().size()-1;i>=0;i--)
	        {
	            AttributeMetadata att= (AttributeMetadata)md.getAttribute_metadata_list().get(i);
	            
	            if(att instanceof CategoricalAttributeMetadata){
	            	int numberOfCategories=((CategoricalAttributeMetadata)att).getNumberOfCategories();
	            	JPAAttributeCategoricalMetaData attrCat=new JPAAttributeCategoricalMetaData();
	            	attrCat.setNumberOfCategories(numberOfCategories);
	            	attrCat.setRatioOfMissingValues(att.getRatioOfMissingValues());
	            	attrCat.setTarget(att.isIsTarget());
	            	attrs.add(attrCat);
	            }else if(att instanceof RealAttributeMetadata){
	            	RealAttributeMetadata ram= ((RealAttributeMetadata)att);
	            	
	            	JPAAttributeNumericalMetaData numericalAttributeMetaData=new JPAAttributeNumericalMetaData();
	            	numericalAttributeMetaData.setAvarage(ram.getAvg());
	            	numericalAttributeMetaData.setClassEntropy(ram.getAttributeClassEntropy());
	            	numericalAttributeMetaData.setMax(ram.getMax());
	            	numericalAttributeMetaData.setMedian(ram.getMedian());
	            	numericalAttributeMetaData.setMin(ram.getMin());
	            	///IMPORTANT!!!!   Getting mode value is not implemented yet!!!
	            	numericalAttributeMetaData.setMode(Double.NaN);
	            	
	            	numericalAttributeMetaData.setReal(true);
	            	numericalAttributeMetaData.setVariance(ram.getStandardDeviation());
	            	numericalAttributeMetaData.setTarget(att.isIsTarget());
	            	numericalAttributeMetaData.setRatioOfMissingValues(att.getRatioOfMissingValues());
	            	attrs.add(numericalAttributeMetaData);
	            }else if(att instanceof IntegerAttributeMetadata){
	            	IntegerAttributeMetadata ram= ((IntegerAttributeMetadata)att);
	            	
	            	JPAAttributeNumericalMetaData numericalAttributeMetaData=new JPAAttributeNumericalMetaData();
	            	numericalAttributeMetaData.setAvarage(ram.getAvg());
	            	numericalAttributeMetaData.setClassEntropy(ram.getAttributeClassEntropy());
	            	numericalAttributeMetaData.setMax(ram.getMax());
	            	numericalAttributeMetaData.setMedian(ram.getMedian());
	            	numericalAttributeMetaData.setMin(ram.getMin());
	            	///IMPORTANT!!!!   Getting mode value is not implemented yet!!!
	            	numericalAttributeMetaData.setMode(Double.NaN);
	            	
	            	numericalAttributeMetaData.setReal(false);
	            	numericalAttributeMetaData.setVariance(ram.getStandardDeviation());
	            	
	            	numericalAttributeMetaData.setTarget(att.isIsTarget());
	            	numericalAttributeMetaData.setRatioOfMissingValues(att.getRatioOfMissingValues());
	            	attrs.add(numericalAttributeMetaData);
	            }else{
	            	System.out.println(att.toString());
	            }
	        }
			
			return attrs;
		}
		
	}