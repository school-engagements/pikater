package pikater.utility.pikaterDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import pikater.agents.metadataQueen.MetadataReader;
import pikater.data.jpa.JPAAttributeCategoricalMetaData;
import pikater.data.jpa.JPAAttributeMetaData;
import pikater.data.jpa.JPAAttributeNumericalMetaData;
import pikater.ontology.messages.DataInstances;
import pikater.ontology.messages.Metadata;
import pikater.ontology.messages.metadata.AttributeMetadata;
import pikater.ontology.messages.metadata.CategoricalAttributeMetadata;
import pikater.ontology.messages.metadata.IntegerAttributeMetadata;
import pikater.ontology.messages.metadata.RealAttributeMetadata;
import weka.core.Instances;

public class JPAMetaDataReader {
	MetadataReader reader;
	Metadata md=null;
	
	public JPAMetaDataReader(){
		reader=new MetadataReader();
	}
	
	public void readFile(File file) throws FileNotFoundException, IOException{	
		DataInstances data=new DataInstances();
		data.fillWekaInstances(new Instances(new BufferedReader(new FileReader(file))));
		md=reader.computeMetadata(data);
		
		for (int i=md.getAttribute_metadata_list().size()-1;i>=0;i--)
        {
            AttributeMetadata att= (AttributeMetadata)md.getAttribute_metadata_list().get(i);
            
            if(att instanceof CategoricalAttributeMetadata){
            	int numberOfCategories=((CategoricalAttributeMetadata)att).getNumberOfCategories();
            	System.out.println("Categorical: no.Cat "+numberOfCategories);
            }else if(att instanceof RealAttributeMetadata){
            	RealAttributeMetadata ram= ((RealAttributeMetadata)att);
                System.out.println("Real: min="+ram.getMin()+" max="+ram.getMax()+" avg="+ram.getAvg()+" median="+ram.getMedian()+" modus= N/impl var="+ram.getStandardDeviation());
            }else{	
            	System.out.println(att.toString());
            }
        }
	}
	
	
	public JPAAttributeMetaData getJPAAttributeMetaData(){
		
		
		JPAAttributeMetaData attr=new JPAAttributeMetaData();
		
		for (int i=md.getAttribute_metadata_list().size()-1;i>=0;i--)
        {
            AttributeMetadata att= (AttributeMetadata)md.getAttribute_metadata_list().get(i);
            
            if(att instanceof CategoricalAttributeMetadata){
            	int numberOfCategories=((CategoricalAttributeMetadata)att).getNumberOfCategories();
            	JPAAttributeCategoricalMetaData attrCat=new JPAAttributeCategoricalMetaData();
            	attrCat.setNumberOfCategories(numberOfCategories);
            	
            	attr.getCategoricalMetaData().add(attrCat);
            	
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
            	
            	
            	attr.getNumericalMetaData().add(numericalAttributeMetaData);
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
            	
            	
            	attr.getNumericalMetaData().add(numericalAttributeMetaData);
            }else{
            	System.out.println(att.toString());
            }
        }
		
		
		
		return attr;
	}
	
}