package checker;

import checker.rules.*;
import lombok.Getter;
import lombok.Setter;
import resource.implementation.InformationResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
@Getter
@Setter
//
public class Checker {
    private Stack<String> stack;
    private List<Rule> rules;
    private DescriptionRepository descriptionRepository;
    public Checker()
    {
        stack = new Stack<>();
        rules = new ArrayList<>();
        descriptionRepository = new DescriptionRepository();

        rules.add(new ContainsRule(descriptionRepository));
        rules.add(new OrderRule(descriptionRepository));
        rules.add(new NecesaryRule(descriptionRepository));
        rules.add(new WhereRule(descriptionRepository));
        rules.add(new ExistsRule(descriptionRepository));

    }

    public void check(String sql, InformationResource ir)
    {
        for(Rule r : rules)
        {
            String s = r.check(sql,ir);
            stack.push(s);
        }
    }
}
