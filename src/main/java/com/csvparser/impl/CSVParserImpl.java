package com.csvparser.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.csvparser.exception.CSVParserInputException;
import com.csvparser.iface.ICSVParser;

public class CSVParserImpl implements ICSVParser{

	public List<Object> parse(String pFilePath, String pClassName) {
		List<String> csvdata = new ArrayList<String>();
		List<Object> objlist = new ArrayList<Object>(); 
		try{
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader(pFilePath));
			while((line = br.readLine()) != null){
				csvdata.add(line);
			}
			br.close();
			int c=0;
			Method[] methods = Class.forName(pClassName).getMethods();
			Field[] fields = Class.forName(pClassName).getDeclaredFields();
			Method[] methodsreq=new Method[fields.length];
			int mcount=0;
			for(Field f:fields){
				for(Method method : methods){
					if(method.getName().equals("set"+capitalize(f.getName()))){
						methodsreq[mcount++]=method;
					}
				}
			}

			for(String data : csvdata){
				if(c++ >=1){
					String[] coldata = data.split(",");
					int i = 0;
					Object obj = Class.forName(pClassName).newInstance();
					for(Method method : methodsreq){
						if(!(method.getParameterTypes()[0].isPrimitive())){
							method.invoke(obj, coldata[i++]);
						}else {
							method.invoke(obj, Integer.parseInt(coldata[i++]));	
						}
					}
					if(addSafe(objlist,obj)){
						objlist.add(obj);
					}
				}
			}
		}catch(FileNotFoundException fnf){
			System.out.println("File Not found Please check the path given");
		}catch(ClassNotFoundException cnf){
			System.out.println("Class Not found Please check fully qualified classname given");
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(IllegalAccessException iae){
			iae.printStackTrace();
		}catch(InstantiationException ie){
			ie.printStackTrace();
		}catch(InvocationTargetException ite){
			ite.printStackTrace();
		}


		return objlist;
	}

	public boolean addSafe(List<Object> pobjlist,Object pobj){
		for(Object obj : pobjlist){
			if(obj.toString().toUpperCase().equals(pobj.toString().toUpperCase())){
				return false;
			}
		}
		return true;
	}

	public String capitalize(String str){
		return Character.toUpperCase(str.charAt(0))+str.substring(1);
	}
	public static void main(String[] args){
		CSVParserImpl csvp = new CSVParserImpl();
		if(args.length!=2){
			try {
				throw new CSVParserInputException();

			} catch (CSVParserInputException e) {


			}
		}else {
			System.out.println(csvp.parse(args[0], args[1]));
		}

	}

}
