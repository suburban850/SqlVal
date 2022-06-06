package checker.rules;

import checker.DBpojo;
import checker.DescriptionRepository;
import resource.implementation.InformationResource;

import java.util.ArrayList;
//GROUP BY
public class NecesaryRule implements Rule{
    DescriptionRepository descriptionRepository;
    public NecesaryRule(DescriptionRepository descriptionRepository) {
        this.descriptionRepository = descriptionRepository;
    }
    // avg max sum count select delu zahtevaju group by
    //select avg(salary),department_id from hr.employees group by
    //1.svaki argument koji nije pod agr mora da bude u group by
    @Override
    public String check(String s, InformationResource ir) {
        DBpojo pojo = descriptionRepository.getPOJOSSS().get(2);
        String[] sk = s.split(" ");
        ArrayList<String> selectDeo = new ArrayList<>();
        int indexFrom = 0;
        for(int i =0; i<sk.length;i++)
        {
            if(sk[i].equals("from"))
            {
                indexFrom=i;
                break;
            }
            System.out.println(sk[i] +"                            ski ");
            selectDeo.add(sk[i]);
        }
        ArrayList<String> orderbys = new ArrayList<>();
        int cnt =0;
        for(int i = 1; i<indexFrom;i++)
        {
            String ss = sk[i];
            if(ss.contains("max") || ss.contains("avg") || ss.contains("sum") || ss.contains("count") || ss.contains("min"))
            {
                System.out.println("sssssss " +ss);
                cnt++;
            }else{
                orderbys.add(ss);
            }
        }
        if(cnt==0) return "true";
        int by =0;
        for(int i = indexFrom;i<sk.length;i++)
        {
            if(sk[i].equals("group"))
            {
                by=i+1;
            }
        }
        if(by==0)
        {
            pojo.setType("GROUP_BY_IS_MISSING");
            pojo.setDesc("You have to add GROUP BY expression");
            pojo.setSug("Columns: "+ orderbys.toString());
            return pojo.toString();
        }
        for(int i = by; i<orderbys.size();i++)
        {
            if(sk[i].equals(orderbys.get(i))) {
                pojo.setType("NONAGGREGATED_COLUMN");
                pojo.setDesc("Column agregation " + sk[i]);
                pojo.setSug("Add " +sk[i] + "to group by " + orderbys.toString());
                return pojo.toString();
            }
        }
        return "true";
    }
}
