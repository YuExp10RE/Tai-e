package pascal.taie.analysis.pta.plugin.taint.inferer.strategy;

import pascal.taie.analysis.pta.plugin.taint.TaintTransfer;
import pascal.taie.analysis.pta.plugin.taint.inferer.InfererContext;
import pascal.taie.language.classes.ClassHierarchy;
import pascal.taie.language.classes.JClass;
import pascal.taie.language.classes.JMethod;
import pascal.taie.util.collection.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

public class IgnoreCollection implements TransInferStrategy {

    public static final String ID = "ignore-collection";

    private static final Set<String> COLLECTION_CLASS = Set.of(
            "java.util.Collection", "java.util.Map", "java.util.Dictionary");

    private ClassHierarchy hierarchy;

    private Set<JClass> collectionClasses;

    private Set<JClass> getCollectionClasses() {
        Set<JClass> collectionClasses = Sets.newSet();
        COLLECTION_CLASS.stream()
                .map(hierarchy::getJREClass)
                .map(hierarchy::getAllSubclassesOf)
                .flatMap(Collection::stream)
                .filter(Predicate.not(JClass::isApplication))
                .forEach(collectionClasses::add);
        Set<JClass> allCollectionClasses = Sets.newSet(collectionClasses);
        collectionClasses.forEach(c ->
                allCollectionClasses.addAll(getAllInnerClassesOf(c)));
        return allCollectionClasses;
    }

    private Set<JClass> getAllInnerClassesOf(JClass jclass) {
        Set<JClass> innerClasses = Sets.newHybridSet();
        hierarchy.getDirectInnerClassesOf(jclass).forEach(inner -> {
            innerClasses.add(inner);
            innerClasses.addAll(getAllInnerClassesOf(inner));
        });
        return innerClasses;
    }

    @Override
    public void setContext(InfererContext context) {
        hierarchy = context.solver().getHierarchy();
        collectionClasses = getCollectionClasses();
    }

    @Override
    public Set<TaintTransfer> apply(JMethod method, Set<TaintTransfer> transfers) {
        if(collectionClasses.contains(method.getDeclaringClass())) {
            return Set.of();
        }
        return Collections.unmodifiableSet(transfers);
    }

    @Override
    public int getPriority() {
        return 10;
    }
}