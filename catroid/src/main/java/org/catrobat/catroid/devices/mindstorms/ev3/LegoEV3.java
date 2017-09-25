/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.devices.mindstorms.ev3;

import org.catrobat.catroid.bluetooth.base.BluetoothDevice;
import org.catrobat.catroid.devices.mindstorms.Mindstorms;
import org.catrobat.catroid.devices.mindstorms.MindstormsSensor;
import org.catrobat.catroid.formulaeditor.Sensors;

public interface LegoEV3 extends Mindstorms, BluetoothDevice {

	String EV3_IMAGE_NAME = "c4Tr01D.rgf"; // TODO: strings to resources ?
	String EV3_SHOW_IMAGE_PROGRAM_NAME = "ShowImageCatroid.rbf";
	String EV3_SHOW_IMAGE_PROGRAM = "4C45474F910100003900040029000000400000000000000027000000F8000000000001001100000013"
			+ "01000000000100280000007D0100000000010015000000A2000F990A3FC10105E1247D05802E72676600F124C10108E1267D0580"
			+ "6334547230314400F126220001543A00581260016030E128C125126C016C41C125061258015840293AE120C12026C120015C4A5C"
			+ "00C1266CC12600056CC126010240102A01C1204C3A4CE120851944864440003A00501278017809020154127C017C3A50483B4840"
			+ "127001700903058300000000830000000054E12601127401741264016409040254C1241268016842C12407125001504081BC0A01"
			+ "823AE1204C26404C484A4800506C500105821B0740024000080A058383828180C10101C1243AE120582648585C4A5C00C1276CC1"
			+ "2701056CC127000340813F7D02D10CF124D124C610D124C1266CC1260081276C4E01024007841200840140003D40C1223D44C120"
			+ "841C01C122C120D10C84008502548654400240004000C100C124080A0282403AE120502640504C4A4C0054305444080A";
	int MAX_LEGO_FILE_SIZE = 10 * 1024 * 1024;

	boolean isAlive();

	void playTone(int frequency, int duration, int volumeInPercent);

	EV3Motor getMotorA();
	EV3Motor getMotorB();
	EV3Motor getMotorC();
	EV3Motor getMotorD();

	void stopAllMovements();

	void moveMotorStepsSpeed(byte outputField, int chainLayer, int speed, int step1Tacho, int step2Tacho,
			int step3Tacho, boolean brake);
	void moveMotorSpeed(byte outputField, int chainLayer, int speed);
	void stopMotor(byte outputField, int chainLayer, boolean brake);

	String downloadFileToEv3(String fileName);
	String downloadFileToEv3(String fileName, byte[] content);
	void showImage(String fileName);
	void startProgram(String programName);

	void setLed(int ledStatus);

	int getSensorValue(Sensors sensor);

	MindstormsSensor getSensor1();
	MindstormsSensor getSensor2();
	MindstormsSensor getSensor3();
	MindstormsSensor getSensor4();
}
