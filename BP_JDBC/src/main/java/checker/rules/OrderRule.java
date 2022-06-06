package checker.rules;

import checker.DBpojo;
import checker.DescriptionRepository;
import lombok.Getter;
import lombok.Setter;
import resource.implementation.InformationResource;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
//
@Getter
@Setter
public class OrderRule implements Rule{
    DescriptionRepository descriptionRepository;
    //1        2      6    4   7  4     3      5   5         5       6
    //select from where like in  join left right  on group by  having
    public OrderRule(DescriptionRepository descriptionRepository) {
        this.descriptionRepository = descriptionRepository;
    }
    //select column names from hr.emplu where hr employees like
    //todo orderby group by
    @Override
    public String check(String s, InformationResource ir) {
        DBpojo pojo = descriptionRepository.getPOJOSSS().get(1);
         List<Integer> ints = new ArrayList<>();

        String split[] = s.split(" ");
        for(int i= 0; i< split.length;i++)
        {
            String st= split[i];
            switch (st)
            {
                case "select" :
                    ints.add(1);
                    break;
                case "from":
                    ints.add(2);
                    break;
                case "left":
                    ints.add(3);
                    break;
                case "join":
                    ints.add(4);
                case "where":
                    ints.add(6);
                    break;
                case "like":
                    ints.add(7);
                    break;
                case "right":
                    ints.add(5);
                    break;
                case "in":
                case "having":
                    ints.add(7);
                    break;
            }
        }
        for(Integer i : ints)
            System.out.print(i+ " ");


      if(!ints.stream().sorted().collect(Collectors.toList()).equals(ints))
      {

          int f = 0;
          for(int i = 0; i<ints.size();i++)
          {
              if(ints.get(i) > ints.get(i+1))
              {
                  f=i;
                  pojo.setSug("Keyword that has a priority of " +ints.get(i) + " should go after " + ints.get(i+1));
                  System.out.println(pojo.toString());;
                  //return pojoToString ili stavi u globalnu listu i onda stampaj na view ako je pun STACK!!!!
                  return pojo.toString();
              }
          }
      }else{
          return "true";
      }
      return "true";
    }
}
