/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is mozilla.org code.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1998
 * the Initial Developer. All Rights Reserved.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package ext.jtester.mozilla.intl.chardet;

public abstract class NsPSMDetector {

	public static final int ALL = 0;
	public static final int JAPANESE = 1;
	public static final int CHINESE = 2;
	public static final int SIMPLIFIED_CHINESE = 3;
	public static final int TRADITIONAL_CHINESE = 4;
	public static final int KOREAN = 5;

	public static final int NO_OF_LANGUAGES = 6;
	public static final int MAX_VERIFIERS = 16;

	NsVerifier[] mVerifier;

	byte[] mState = new byte[MAX_VERIFIERS];
	int[] mItemIdx = new int[MAX_VERIFIERS];

	int mItems;
	int mClassItems;

	boolean mDone;
	boolean mRunSampler;
	boolean mClassRunSampler;

	public NsPSMDetector() {
		initVerifiers(NsPSMDetector.ALL);
		reset();
	}

	public NsPSMDetector(int langFlag) {
		initVerifiers(langFlag);
		reset();
	}

	public void reset() {
		mRunSampler = mClassRunSampler;
		mDone = false;
		mItems = mClassItems;

		for (int i = 0; i < mItems; i++) {
			mState[i] = 0;
			mItemIdx[i] = i;
		}
	}

	protected void initVerifiers(int currVerSet) {

		@SuppressWarnings("unused")
		int idx = 0;
		int currVerifierSet;

		if (currVerSet >= 0 && currVerSet < NO_OF_LANGUAGES) {
			currVerifierSet = currVerSet;
		} else {
			currVerifierSet = NsPSMDetector.ALL;
		}

		mVerifier = null;

		if (currVerifierSet == NsPSMDetector.TRADITIONAL_CHINESE) {
			mVerifier = new NsVerifier[] { new NsUTF8Verifier(), new NsBIG5Verifier(), new NsISO2022CNVerifier(),
					new NsGB2312Verifier() };
		} else if (currVerifierSet == NsPSMDetector.SIMPLIFIED_CHINESE) {
			mVerifier = new NsVerifier[] { new NsUTF8Verifier(), new NsGB2312Verifier(), new NsISO2022CNVerifier(),
					new NsHZVerifier() };
		} else {
			mVerifier = new NsVerifier[] { new NsUTF8Verifier(), new NsGB2312Verifier(), new NsBIG5Verifier(),
					new NsISO2022CNVerifier(), new NsHZVerifier() };
		}
		mClassItems = mVerifier.length;
	}

	public abstract void report(String charset);

	public boolean HandleData(byte[] aBuf, int len) {
		int i, j;
		byte b, st;

		for (i = 0; i < len; i++) {
			b = aBuf[i];
			for (j = 0; j < mItems;) {
				st = NsVerifier.getNextState(mVerifier[mItemIdx[j]], b, mState[j]);
				if (st == NsVerifier.eItsMe) {
					report(mVerifier[mItemIdx[j]].charset());
					mDone = true;
					return mDone;

				} else if (st == NsVerifier.eError) {
					mItems--;
					if (j < mItems) {
						mItemIdx[j] = mItemIdx[mItems];
						mState[j] = mState[mItems];
					}
				} else {
					mState[j++] = st;
				}
			}

			int nonUCS2Num = 0;
			int nonUCS2Idx = 0;

			for (j = 0; j < mItems; j++) {
				if ((!(mVerifier[mItemIdx[j]].isUCS2())) && (!(mVerifier[mItemIdx[j]].isUCS2()))) {
					nonUCS2Num++;
					nonUCS2Idx = j;
				}
			}

			if (1 == nonUCS2Num) {
				report(mVerifier[mItemIdx[nonUCS2Idx]].charset());
				mDone = true;
				return mDone;
			}
		}
		return mDone;
	}

	public void dataEnd() {
		if (mDone == true)
			return;

		if (mItems == 2) {
			if ((mVerifier[mItemIdx[0]].charset()).equals("GB18030")) {
				report(mVerifier[mItemIdx[1]].charset());
				mDone = true;
			} else if ((mVerifier[mItemIdx[1]].charset()).equals("GB18030")) {
				report(mVerifier[mItemIdx[0]].charset());
				mDone = true;
			}
		}
	}

	public String[] getProbableCharsets() {
		String ret[] = new String[mItems];
		for (int i = 0; i < mItems; i++)
			ret[i] = mVerifier[mItemIdx[i]].charset();
		return ret;
	}
}
