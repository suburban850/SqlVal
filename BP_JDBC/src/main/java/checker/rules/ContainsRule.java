package checker.rules;

import checker.DBpojo;
import checker.DescriptionRepository;
import resource.DBNode;
import resource.implementation.Attribute;
import resource.implementation.Entity;
import resource.implementation.InformationResource;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
//
public class ContainsRule implements Rule{
    //da li kolone i tabele koje su navedene postoje u bazi
    private DescriptionRepository descriptionRepository;
    public ContainsRule(DescriptionRepository descriptionRepository)
    {
        this.descriptionRepository = descriptionRepository;
    }

    //select country_id, country_name from bp_tim68.countries
    @Override
    public void check(String sql, InformationResource ir) {
         String[] query = sql.split(" ");
        List<String> columns = new ArrayList<>();
        int f = 0;
         for(int i = 1; i<query.length;i++)
         {
             if(query[i+1].equals("from"))
             {
                 columns.add(query[i]);
                 System.out.print(query[i] + " ");
                 f = i+1;
                 break;
             }
             System.out.print(query[i] + " ");
             columns.add(query[i]);
         }
         List<String> newColumns = new ArrayList<>();
         for(int i = 0; i<columns.size();i++)
         {
             String s = columns.get(i);
             s.trim();
             if(s.charAt(s.length()-1) == ',')
             {
                 StringBuilder sb = new StringBuilder(s);
                 sb.deleteCharAt(s.length()-1);
                 newColumns.add(sb.toString());
             }else{
                newColumns.add(s);
             }

         }
        DBpojo pojo = descriptionRepository.getPOJOSSS().get(0);

        System.out.println("New columns");
         for(String s : newColumns) System.out.print(s + " ");
        String tableName = query[f+1];
        System.out.println(tableName);
         String[] split = tableName.split("\\.");
        String table = split[1];

        Entity entity = (Entity) ir.getChildByName(table);
        if(entity == null){
            //posalji negde table NO_TABLE JSON
            pojo.setDesc("Table doesn't exist");
            System.out.printf(pojo.toString(),"NO_TABLE");
            return;
        }
        for(int i =0; i<columns.size();i++)
        {
           Attribute at =(Attribute) entity.getChildByName(newColumns.get(i));
           if(at == null)
           {
               System.out.println("nema kolonu");
               //posalji negde newColumns.get(i) JSON
               //NO_TYPE
               System.out.printf(pojo.toString(),"NO_COLUMNS",newColumns.get(i),entity.getName());
               return;
           }
        }
        System.out.println("ima i tabelu i kolonu");

    }
}
