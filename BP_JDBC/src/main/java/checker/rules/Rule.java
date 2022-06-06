package checker.rules;

import resource.implementation.InformationResource;
//
public interface Rule {
    String check(String s, InformationResource ir);
}
