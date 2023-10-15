package com.satergo.controller;

import com.satergo.Load;
import com.satergo.Main;
import com.satergo.ProgramData;
import com.satergo.extra.ToggleSwitch;
import com.satergo.extra.dialog.MoveStyle;
import com.satergo.extra.dialog.SatPromptDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.util.Pair;

public class LightNodeSourceCtrl implements SetupPage.WithExtra {

	@FXML private Parent root;

	@FXML
	public void downloadAndSetup(ActionEvent e) {
		SatPromptDialog<Pair<Boolean, Boolean>> dialog = new SatPromptDialog<>();
		dialog.initOwner(Main.get().stage());
		dialog.setMoveStyle(MoveStyle.FOLLOW_OWNER);
		Main.get().applySameTheme(dialog.getScene());
		Parent root = Load.fxml("/dialog/light-node-config.fxml");
		dialog.getDialogPane().setContent(root);
		ToggleSwitch nipopow = (ToggleSwitch) root.lookup("#nipopow"),
				utxoSetSnapshot = (ToggleSwitch) root.lookup("#utxoSetSnapshot");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialog.setResultConverter(param -> {
			if (param == ButtonType.OK) return new Pair<>(nipopow.isSelected(), utxoSetSnapshot.isSelected());
			else return null;
		});
		dialog.showForResult().ifPresent(pref -> {
			NodeDownloaderCtrl ctrl = new NodeDownloaderCtrl(Main.lang("lightNodeSetup"), pref.getKey(), pref.getValue());
			Load.fxmlControllerFactory("/setup-page/node-download.fxml", ctrl);
			Main.get().displaySetupPage(ctrl);
		});
	}

	@FXML
	public void useExisting(ActionEvent e) {
		FullNodeSourceCtrl.useExisting(ProgramData.NodeKind.EMBEDDED_LIGHT_NODE);
	}

	@Override
	public Parent recreate() {
		return Load.fxml("/setup-page/light-node-source.fxml");
	}

	@Override
	public Parent content() {
		return root;
	}
}
