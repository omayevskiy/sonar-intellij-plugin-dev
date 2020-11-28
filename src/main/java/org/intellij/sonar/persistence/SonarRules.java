package org.intellij.sonar.persistence;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.intellij.sonar.sonarserver.Rule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
  name = "rules",
  storages = {
    @Storage("sonarRules.xml")
  }
)
public class SonarRules implements PersistentStateComponent<SonarRules>, ProjectComponent {

  private Map<String,Rule> sonarRulesByRuleKey = new ConcurrentHashMap<>();

  public static Optional<SonarRules> getInstance(Project project) {
    return Optional.ofNullable(project.getComponent(SonarRules.class));
  }

  @Nullable
  @Override
  public SonarRules getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull SonarRules state) {
    XmlSerializerUtil.copyBean(state,this);
  }

  public Map<String,Rule> getSonarRulesByRuleKey() {
    return sonarRulesByRuleKey;
  }

  public void setSonarRulesByRuleKey(Map<String,Rule> sonarRulesByRuleKey) {
    this.sonarRulesByRuleKey = sonarRulesByRuleKey;
  }

  @Override
  public void projectOpened() {
  }

  @Override
  public void projectClosed() {
  }

  @Override
  public void initComponent() {
  }

  @Override
  public void disposeComponent() {
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "SonarRules";
  }
}
