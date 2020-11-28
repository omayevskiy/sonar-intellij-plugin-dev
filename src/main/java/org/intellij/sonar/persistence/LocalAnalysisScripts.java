package org.intellij.sonar.persistence;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@State(
  name = "localAnalysisScripts",
  storages = {
    @Storage("sonarSettings.xml")
  }
)
public class LocalAnalysisScripts implements PersistentStateComponent<LocalAnalysisScripts> {

  public static final String NO_LOCAL_ANALYSIS = "<NO LOCAL ANALYSIS>";
  public static final String PROJECT = "<PROJECT>";
  public Collection<LocalAnalysisScript> beans = new ArrayList<>();

  @NotNull
  public static LocalAnalysisScripts getInstance() {
    return ServiceManager.getService(LocalAnalysisScripts.class);
  }

  public static void add(final LocalAnalysisScript newLocalAnalysisScript) {
    final Collection<LocalAnalysisScript> localAnalysisScripts = LocalAnalysisScripts.getInstance().getState().beans;
    final boolean alreadyExists = localAnalysisScripts.stream()
        .anyMatch(localAnalysisScript -> localAnalysisScript.equals(newLocalAnalysisScript));
    if (alreadyExists) {
      throw new IllegalArgumentException("already exists");
    } else {
      localAnalysisScripts.add(newLocalAnalysisScript);
    }
  }

  public static void remove(@NotNull final String name) {
    final Optional<LocalAnalysisScript> bean = get(name);
    Preconditions.checkArgument(bean.isPresent());
      getInstance().beans = getAll().stream()
              .filter(localAnalysisScript -> !bean.get().equals(localAnalysisScript))
              .collect(Collectors.toList());
  }

  public static Optional<LocalAnalysisScript> get(@NotNull final String name) {
    final Collection<LocalAnalysisScript> allBeans = getAll();
    return allBeans.stream().filter(localAnalysisScript -> name.equals(localAnalysisScript.getName())).findFirst();
  }

  public static Collection<LocalAnalysisScript> getAll() {
    return LocalAnalysisScripts.getInstance().getState().beans;
  }

  @NotNull
  @Override
  public LocalAnalysisScripts getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull LocalAnalysisScripts state) {
    XmlSerializerUtil.copyBean(state,this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LocalAnalysisScripts that = (LocalAnalysisScripts) o;
    return Objects.equal(beans, that.beans);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(beans);
  }
}
