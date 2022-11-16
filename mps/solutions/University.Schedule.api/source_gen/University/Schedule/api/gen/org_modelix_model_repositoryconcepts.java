package University.Schedule.api.gen;

/*Generated by MPS */

import org.modelix.mps.apigen.runtime.MPSLanguage;
import java.util.List;
import org.modelix.mps.apigen.runtime.AbstractConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.DevKitConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.DevkitDependencyConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.JavaModuleFacetConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.LanguageConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.LanguageDependencyConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.ModelConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.ModelReferenceConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.ModuleConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.ModuleDependencyConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.ModuleFacetConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.ModuleReferenceConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.ProjectConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.ProjectModuleConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.RepositoryConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.SingleLanguageDependencyConcept;
import org.modelix.model.repositoryconcepts.structure.concepts.SolutionConcept;
import java.util.function.Consumer;

public class org_modelix_model_repositoryconcepts extends MPSLanguage {
  public static final org_modelix_model_repositoryconcepts INSTANCE = new org_modelix_model_repositoryconcepts();
  private org_modelix_model_repositoryconcepts() {
    super("org.modelix.model.repositoryconcepts", "org.modelix.model.repositoryconcepts");
    List<AbstractConcept<?>> myConcepts = this.getMyConcepts();
    myConcepts.add(DevKitConcept.INSTANCE);
    myConcepts.add(DevkitDependencyConcept.INSTANCE);
    myConcepts.add(JavaModuleFacetConcept.INSTANCE);
    myConcepts.add(LanguageConcept.INSTANCE);
    myConcepts.add(LanguageDependencyConcept.INSTANCE);
    myConcepts.add(ModelConcept.INSTANCE);
    myConcepts.add(ModelReferenceConcept.INSTANCE);
    myConcepts.add(ModuleConcept.INSTANCE);
    myConcepts.add(ModuleDependencyConcept.INSTANCE);
    myConcepts.add(ModuleFacetConcept.INSTANCE);
    myConcepts.add(ModuleReferenceConcept.INSTANCE);
    myConcepts.add(ProjectConcept.INSTANCE);
    myConcepts.add(ProjectModuleConcept.INSTANCE);
    myConcepts.add(RepositoryConcept.INSTANCE);
    myConcepts.add(SingleLanguageDependencyConcept.INSTANCE);
    myConcepts.add(SolutionConcept.INSTANCE);
    myConcepts.forEach(new Consumer<AbstractConcept<?>>() {
      public void accept(AbstractConcept<?> concept) {
        concept.init(org_modelix_model_repositoryconcepts.this);
      }
    });
  }
}
