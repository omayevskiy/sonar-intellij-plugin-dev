package org.intellij.sonar.configuration.module;

import static org.intellij.sonar.persistence.SonarServers.NO_SONAR;
import static org.intellij.sonar.persistence.SonarServers.PROJECT;
import static org.intellij.sonar.util.UIUtil.makeObj;

import java.util.Collection;
import java.util.Optional;

import javax.swing.*;

import com.intellij.openapi.project.Project;
import org.intellij.sonar.configuration.partials.SonarServersView;
import org.intellij.sonar.persistence.ProjectSettings;
import org.intellij.sonar.persistence.Settings;
import org.intellij.sonar.persistence.SonarServerConfig;
import org.intellij.sonar.persistence.SonarServers;

public class ModuleSonarServersView extends SonarServersView {

  public ModuleSonarServersView(
    JComboBox mySonarServersComboBox,
    JButton myAddSonarServerButton,
    JButton myEditSonarServerButton,
    JButton myRemoveSonarServerButton,
    Project myProject
  ) {
    super(mySonarServersComboBox,myAddSonarServerButton,myEditSonarServerButton,myRemoveSonarServerButton,myProject);
  }

  @Override
  public String getSelectedItem() {
    final String selectedItem = super.getSelectedItem();
    if (SonarServers.PROJECT.equals(selectedItem)) {
      final Settings settings = ProjectSettings.getInstance(myProject).getState();
      return null != settings
        ? settings.getServerName()
        : NO_SONAR;
    }
    return selectedItem;
  }

  @Override
  protected boolean editAndRemoveButtonsCanBeEnabled() {
    final boolean isNoSonarSelected = NO_SONAR.equals(String.valueOf(mySonarServersComboBox.getSelectedItem()));
    final boolean isProjectSonarSelected = PROJECT.equals(String.valueOf(mySonarServersComboBox.getSelectedItem()));
    return !isNoSonarSelected && !isProjectSonarSelected;
  }

  @Override
  protected void initSonarServersComboBox() {
    Optional<Collection<SonarServerConfig>> sonarServerConfigurationBeans = SonarServers.getAll();
    if (sonarServerConfigurationBeans.isPresent()) {
      mySonarServersComboBox.removeAllItems();
      mySonarServersComboBox.addItem(makeObj(PROJECT));
      mySonarServersComboBox.addItem(makeObj(NO_SONAR));
      for (SonarServerConfig sonarServerConfigBean : sonarServerConfigurationBeans.get()) {
        mySonarServersComboBox.addItem(makeObj(sonarServerConfigBean.getName()));
      }
    }
  }
}
