package com.kh09909.shahin_rasid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import maes.tech.intentanim.CustomIntent;

import static android.view.animation.AnimationUtils.loadAnimation;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    int PERMISSION_ALL = 1;
    
    SharedPreferences Shared; // TO SAVE VALUE AFTER CLOSE THE APPLICATION

    ImageView IVZain, IVSTC, IVMobily;

    int IVZainSelectd = 0 , IVSTCSelectd = 0 , IVMobilySelectd = 0;


    Button Charge, Balance, Callme, Transfer, Service;

    String Hash = "#";

    String CallNumber = ""; //USED IN MAKE CALL METHODE - TO GET FULL NUMBER THEN CALL

    Button Mic, CamAndGallary;

    RadioGroup RadioGroup;

    RadioButton RBZain, RBSTC, RBMobily;

    ClipboardManager ClipToCopy; // use it to Copy number
    ClipData clipString; // use it to Copy number

    int CheckForRadioButton = 0 ; // TO CHECK IS THERE ANY RADIO BUTTON CHECKED OR NOT TO COMPELETE APPLICATION


    Animation animation ;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CAMERA
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        Shared = getSharedPreferences("ShahinRasid", Context.MODE_PRIVATE); // قيمه تحفظ ولا تتاثر باغلاق البرنامج
        int StyleNumber = Shared.getInt("StyleNumber", 1);


        if (StyleNumber == 1) {
            this.setTheme(R.style.ZainTheme);
        }

        else if (StyleNumber == 2) {
            this.setTheme(R.style.STCTheme);
        }

        else if (StyleNumber == 3) {
            this.setTheme(R.style.MobilyTheme);
        }



        setContentView(R.layout.activity_main);


        animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);


        Mic = (Button) findViewById(R.id.BtnMic);
        CamAndGallary = (Button) findViewById(R.id.BtnCamAndGallary);
        Charge = (Button) findViewById(R.id.BtnCharge);
        Balance = (Button) findViewById(R.id.BtnBalance);
        Callme = (Button) findViewById(R.id.BtnCallme);
        Transfer = (Button) findViewById(R.id.BtnBalancetransfer);
        Service = (Button) findViewById(R.id.BtnServiceCenter);


        RadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        RBZain = (RadioButton) findViewById(R.id.RBZain);
        RBSTC = (RadioButton) findViewById(R.id.RBSTC);
        RBMobily = (RadioButton) findViewById(R.id.RBMobily);


        IVZain = (ImageView) findViewById(R.id.IVZain);
        IVSTC = (ImageView) findViewById(R.id.IVSTC);
        IVMobily = (ImageView) findViewById(R.id.IVMobily);



        Mic.setOnClickListener(this);
        CamAndGallary.setOnClickListener(this);
        Charge.setOnClickListener(this);
        Balance.setOnClickListener(this);
        Callme.setOnClickListener(this);
        Transfer.setOnClickListener(this);
        Service.setOnClickListener(this);


        RBZain.setOnClickListener(this);
        RBSTC.setOnClickListener(this);
        RBMobily.setOnClickListener(this);


        ClipToCopy = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);



        final int StopRadioButton;
        StopRadioButton = StyleNumber;


        // ACTIONS FOR RADIOBUTTONS

        IVZain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IVZainSelectd = 1;
                IVSTCSelectd = 0 ;
                IVMobilySelectd = 0;

                if (StopRadioButton == 2 || StopRadioButton == 3){
                    Style(1);

            }
            }

        });

        IVSTC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                IVZainSelectd = 0;
                IVSTCSelectd = 1 ;
                IVMobilySelectd = 0;

                if (StopRadioButton == 1 || StopRadioButton == 3) {
                    Style(2);
                }
            }
        });

        IVMobily.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IVZainSelectd = 0;
                IVSTCSelectd = 0 ;
                IVMobilySelectd = 1;


                if (StopRadioButton == 1 || StopRadioButton == 2) {
                    Style(3);
                }
            }
        });





        //-------------------------------------------------------- بعد نغيير التصميم تعود القيم الى البداية ، لذلك نربط تشغيل RadioButton بنوع التصميم

        if (StyleNumber == 1) {
            RBZain.setChecked(true);
            CheckForRadioButton = 1 ;
            IVMobily.setImageResource(R.drawable.mobily_off); //To change image/button to off
            IVSTC.setImageResource(R.drawable.stc_off); //To change image/button to off

        }

        else if (StyleNumber == 2) {
            RBSTC.setChecked(true);
            CheckForRadioButton = 1 ;
            IVMobily.setImageResource(R.drawable.mobily_off); //To change image/button to off
            IVZain.setImageResource(R.drawable.zain_off); //To change image/button to off
        }

        else if (StyleNumber == 3) {
            RBMobily.setChecked(true);
            CheckForRadioButton = 1 ;
            IVSTC.setImageResource(R.drawable.stc_off); //To change image/button to off
            IVZain.setImageResource(R.drawable.zain_off); //To change image/button to off
        }


        //----------------------------------------------------------------------------------------------------------------------------------------------



        //For Camera and read the text -----------------------------------------------------------


        NumberImage = (ImageView)findViewById(R.id.NumberImage);

        ETCardNumber = (EditText) findViewById(R.id.ETCardNumber);

        //----------------------------------------------------------------------------------------



    }


    final int REQUEST_VOICE = 222;


    public void Mic() {

        if (CheckForRadioButton == 0) {

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.CheckRadioButton), Toast.LENGTH_SHORT).show();

        } else if (CheckForRadioButton == 1) {


            Intent intentVoiceToText = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentVoiceToText.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL , RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intentVoiceToText.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


            String dl = Locale.getDefault().toString().charAt(0)+""+Locale.getDefault().toString().charAt(1);
            if(dl.equals("ar")){
                intentVoiceToText.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar_SA");
                intentVoiceToText.putExtra(RecognizerIntent.EXTRA_PROMPT," أدخل رقم البطاقة بالعربي ...");
                intentVoiceToText.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 25);
            }

            else{

                intentVoiceToText.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
                intentVoiceToText.putExtra(RecognizerIntent.EXTRA_PROMPT,"Enter Card Number ...");
                intentVoiceToText.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 25);

            }



            try{

                startActivityForResult(intentVoiceToText, REQUEST_VOICE);

            }
            catch (ActivityNotFoundException a) {

                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.YourDivceNotSupport), Toast.LENGTH_SHORT).show();

            }


        }

    }



    //For Camera and read the text -----------------------------------------------------------


    public void CamAndGallary() {
        if (CheckForRadioButton == 0) {

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.CheckRadioButton), Toast.LENGTH_SHORT).show();

        }
        else {
            new CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/ShahinRasid/Pictures", "/sdcard/ShahinRasid/Pictures");
            CroperinoFileUtil.verifyStoragePermissions(MainActivity.this);
            CroperinoFileUtil.setupDirectory(MainActivity.this);

            Croperino.prepareChooser(MainActivity.this, getString(R.string.UseCameraOrGallery),
                    ContextCompat.getColor(MainActivity.this, android.R.color.white));

        }
    }



    EditText ETCardNumber;
    String stringWithoutSpaces ;

    public void Read(){

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational())
            Log.e("ERROR","Detector dependencies are not yet available");
        else{

            Bitmap ImageToRead=((BitmapDrawable)NumberImage.getDrawable()).getBitmap();


            Frame frame = new Frame.Builder().setBitmap(ImageToRead).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0;i<items.size();++i)
            {
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
            }

            stringWithoutSpaces = stringBuilder.toString().replaceAll(" ","");
            ETCardNumber.setText(stringWithoutSpaces);
        }

    }


    //----------------------------------------------------------------------------------------


    String ZainCharge = "*141*";
    String MobilyCharge = "*1400*";
    String STCCharge = "*155*";


    public void Charge() {

        if (CheckForRadioButton == 0) {

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.CheckRadioButton), Toast.LENGTH_SHORT).show();

        } else if (CheckForRadioButton == 1) {


            int selectedId = RadioGroup.getCheckedRadioButtonId(); // TO CHECK WHICH RADIO BUTTON IS CHECKED

            switch (selectedId) {

                case R.id.RBZain:
                    if (RBZain.isChecked()) {
                        // ZAIN Charge

                        if (ETCardNumber.length() > 0) {
                            CallNumber = ZainCharge + ETCardNumber.getText().toString() + Hash;
                            MakeCall();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.ShouldGetCardNumber), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                case R.id.RBSTC:
                    if (RBSTC.isChecked()) {
                        // STC CHARGE
                        if (ETCardNumber.length() > 0) {
                            CallNumber = STCCharge + ETCardNumber.getText().toString() + Hash;
                            MakeCall();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.ShouldGetCardNumber), Toast.LENGTH_SHORT).show();
                        }

                    }
                    break;

                case R.id.RBMobily:
                    if (RBMobily.isChecked()) {
                            // Mobily CHARGE

                        if (ETCardNumber.length() > 0) {
                            CallNumber = MobilyCharge + ETCardNumber.getText().toString() + Hash;
                            MakeCall();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.ShouldGetCardNumber), Toast.LENGTH_SHORT).show();
                        }


                    }
                    break;


                default:
                    break;
            }

        }

    }



    String ZainBalance = "*142#";
    String MobilyBalance = "*1411#";
    String STCBalance = "*166#";


    public void Balance() {



        if (CheckForRadioButton == 0) {

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.CheckRadioButton), Toast.LENGTH_SHORT).show();

        } else if (CheckForRadioButton == 1) {


            int selectedId = RadioGroup.getCheckedRadioButtonId(); // TO CHECK WHICH RADIO BUTTON IS CHECKED

            switch (selectedId) {

                case R.id.RBZain:
                    if (RBZain.isChecked()) {
                        // ZAIN BALANCE
                        CallNumber = ZainBalance;
                        MakeCall();
                    }
                    break;

                case R.id.RBSTC:
                    if (RBSTC.isChecked()) {
                        // STC BALANCE
                        CallNumber = STCBalance;
                        MakeCall();
                    }
                    break;

                case R.id.RBMobily:
                    if (RBMobily.isChecked()) {

                        // Mobily BALANCE
                        CallNumber = MobilyBalance;
                        MakeCall();

                    }
                    break;


                default:
                    break;
            }

        }
    }



    String CallMeNumber = "";
    String CallmeZain = "*123*";
    String CallmeSTC = "*177*";
    String CallmeMobily = "*199*";


    public void Callme(String PhoneNumber1) {

        if (CheckForRadioButton == 0) {

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.CheckRadioButton), Toast.LENGTH_SHORT).show();

        } else if (CheckForRadioButton == 1) {

            int selectedId = RadioGroup.getCheckedRadioButtonId(); // TO CHECK WHICH RADIO BUTTON IS CHECKED

            switch (selectedId) {

                case R.id.RBZain:
                    if (RBZain.isChecked()) {
                        //Zain Callme
                        CallMeNumber = CallmeZain + PhoneNumber1 + Hash;
                        CallNumber = CallMeNumber.replaceAll(Pattern.quote("+966"), "0");
                        MakeCall();
                    }
                    break;

                case R.id.RBSTC:
                    if (RBSTC.isChecked()) {
                        //STC Callme
                        CallMeNumber = CallmeSTC + PhoneNumber1 + Hash;
                        CallNumber = CallMeNumber.replaceAll(Pattern.quote("+966"), "0");
                        MakeCall();
                    }
                    break;

                case R.id.RBMobily:
                    if (RBMobily.isChecked()) {
                        //Mobily Callme
                        CallMeNumber = CallmeMobily + PhoneNumber1 + Hash;
                        CallNumber = CallMeNumber.replaceAll(Pattern.quote("+966"), "0");
                        MakeCall();
                    }
                    break;


                default:
                    break;
            }

        }

    }





    String AmountSMS = ""; // How much amount you want
    String NumberSMS = ""; // Send message to this number
    String Message = ""; // Message




    public void SendSMS() {

        Uri uri = Uri.parse("smsto:"+NumberSMS);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", Message);
        startActivity(it);

    }




    int SendOrAsk = 0; //  IN ZAIN CASE WE SHOULD TO CHECK WHICH BUTTON IS CLICKED

    public void Transfer(final String PhoneNumber2) {


        if (CheckForRadioButton == 0) {

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.CheckRadioButton), Toast.LENGTH_SHORT).show();

        } else if (CheckForRadioButton == 1) {


            int selectedId = RadioGroup.getCheckedRadioButtonId(); // TO CHECK WHICH RADIO BUTTON IS CHECKED

            switch (selectedId) {


                case R.id.RBZain:
                    if (RBZain.isChecked()) {

                        // ZAIN Transfer

                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                        View parentView = getLayoutInflater().inflate(R.layout.bottom_sheet_zain_transfer, null);
                        bottomSheetDialog.setContentView(parentView);
                        BottomSheetBehavior bottomSheetBehavior = new BottomSheetBehavior();
                        bottomSheetBehavior.setPeekHeight(
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100 , getResources().getDisplayMetrics()));
                        bottomSheetDialog.show();

                        final Button BtnAskAmountZain = (Button) parentView.findViewById(R.id.BtnAskAmountZain);

                        final Button BtnSendAmountZain = (Button) parentView.findViewById(R.id.BtnSendAmountZain);


                        final EditText ETAmountZain;
                        ETAmountZain = (EditText) parentView.findViewById(R.id.ETAmountZain);
                        ETAmountZain.setVisibility(View.GONE);


                        final Button BtnSendSMSZain = (Button) parentView.findViewById(R.id.BtnSendSMSZain);
                        BtnSendSMSZain.setVisibility(View.GONE);



                        BtnAskAmountZain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                SendOrAsk = 1;

                                NumberSMS = PhoneNumber2;

                                ETAmountZain.setVisibility(View.VISIBLE);
                                BtnSendSMSZain.setVisibility(View.VISIBLE);
                                BtnSendAmountZain.setVisibility(View.GONE);
                                BtnAskAmountZain.setVisibility(View.GONE);

                            }
                        });



                        BtnSendAmountZain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                SendOrAsk = 2;

                                NumberSMS = "702702";

                                ETAmountZain.setVisibility(View.VISIBLE);
                                BtnSendSMSZain.setVisibility(View.VISIBLE);
                                BtnSendAmountZain.setVisibility(View.GONE);
                                BtnAskAmountZain.setVisibility(View.GONE);



                            }
                        });




                        BtnSendSMSZain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(ETAmountZain.getText().length() == 0){
                                    Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.EnterAmount), Toast.LENGTH_SHORT).show();
                                }

                                else {

                                    if(SendOrAsk == 1){

                                        AmountSMS = ETAmountZain.getText().toString();

                                        if(AmountSMS.equals("5")|| AmountSMS.equals("10") || AmountSMS.equals("15") || AmountSMS.equals("20")){

                                            Message = getString(R.string.PleaseSendAmount)+" "+AmountSMS+" "+getString(R.string.SRThanks);

                                            SendSMS();

                                            Toast.makeText(getApplicationContext(),getString(R.string.NowSend), Toast.LENGTH_SHORT).show();
                                        }

                                        else {
                                            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.WriteCorrect), Toast.LENGTH_SHORT).show();
                                            ETAmountZain.setText(null);
                                        }

                                    }

                                    if(SendOrAsk == 2){


                                        AmountSMS = ETAmountZain.getText().toString();

                                        if(AmountSMS.equals("5")|| AmountSMS.equals("10") || AmountSMS.equals("15") || AmountSMS.equals("20")){

                                            Message = "bt"+" "+PhoneNumber2.replaceAll(Pattern.quote("+966"), "0")+" "+AmountSMS;

                                            SendSMS();

                                            Toast.makeText(getApplicationContext(),getString(R.string.NowSend), Toast.LENGTH_SHORT).show();
                                        }

                                        else {
                                            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.WriteCorrect), Toast.LENGTH_SHORT).show();
                                            ETAmountZain.setText(null);
                                        }


                                    }


                                }


                            }
                        });




                    }
                    break;



                case R.id.RBSTC:
                    if (RBSTC.isChecked()) {
                        // STC Transfer

                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                        View parentView = getLayoutInflater().inflate(R.layout.bottom_sheet_stc_transfer, null);
                        bottomSheetDialog.setContentView(parentView);
                        BottomSheetBehavior bottomSheetBehavior = new BottomSheetBehavior();
                        bottomSheetBehavior.setPeekHeight(
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100 , getResources().getDisplayMetrics()));
                        bottomSheetDialog.show();


                        final Button BtnAskAmountSTC = (Button) parentView.findViewById(R.id.BtnAskAmountSTC);

                        final Button BtnSendAmountSTC = (Button) parentView.findViewById(R.id.BtnSendAmountSTC);

                        final EditText ETAmountSTC;
                        ETAmountSTC = (EditText) parentView.findViewById(R.id.ETAmountSTC);
                        ETAmountSTC.setVisibility(View.GONE);


                        final Button BtnSendSMSSTC = (Button) parentView.findViewById(R.id.BtnSendSMSSTC);
                        BtnSendSMSSTC.setVisibility(View.GONE);


                        BtnAskAmountSTC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                SendOrAsk = 1;

                                NumberSMS = "900";

                                ETAmountSTC.setVisibility(View.VISIBLE);
                                BtnSendSMSSTC.setVisibility(View.VISIBLE);
                                BtnSendAmountSTC.setVisibility(View.GONE);
                                BtnAskAmountSTC.setVisibility(View.GONE);


                            }
                        });



                        BtnSendAmountSTC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                SendOrAsk = 2;

                                NumberSMS = "900";

                                ETAmountSTC.setVisibility(View.VISIBLE);
                                BtnSendSMSSTC.setVisibility(View.VISIBLE);
                                BtnSendAmountSTC.setVisibility(View.GONE);
                                BtnAskAmountSTC.setVisibility(View.GONE);



                            }
                        });



                                BtnSendSMSSTC.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        if(ETAmountSTC.getText().length() == 0){
                                            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.EnterAmount), Toast.LENGTH_SHORT).show();
                                        }


                                        else {



                                            if(SendOrAsk == 1){

                                                AmountSMS = ETAmountSTC.getText().toString();

                                                if(AmountSMS.equals("5")|| AmountSMS.equals("10") || AmountSMS.equals("15") || AmountSMS.equals("20")){

                                                    Message ="170"+" "+PhoneNumber2.replaceAll(Pattern.quote("+966"), "0")+" "+AmountSMS;

                                                    SendSMS();

                                                    Toast.makeText(getApplicationContext(),getString(R.string.NowSend), Toast.LENGTH_SHORT).show();
                                                }

                                                else {
                                                    Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.WriteCorrect), Toast.LENGTH_SHORT).show();
                                                    ETAmountSTC.setText(null);
                                                }

                                            }

                                            if(SendOrAsk == 2){


                                                AmountSMS = ETAmountSTC.getText().toString();

                                                if(AmountSMS.equals("5")|| AmountSMS.equals("10") || AmountSMS.equals("15") || AmountSMS.equals("20")){

                                                    Message ="133"+" "+PhoneNumber2.replaceAll(Pattern.quote("+966"), "0")+" "+AmountSMS;

                                                    SendSMS();

                                                    Toast.makeText(getApplicationContext(),getString(R.string.NowSend), Toast.LENGTH_SHORT).show();
                                                }

                                                else {
                                                    Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.WriteCorrect), Toast.LENGTH_SHORT).show();
                                                    ETAmountSTC.setText(null);
                                                }


                                            }


                                        }


                                    }
                                });

                    }
                    break;

                case R.id.RBMobily:
                    if (RBMobily.isChecked()) {

                        // Mobily Transfer


                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                        View parentView = getLayoutInflater().inflate(R.layout.bottom_sheet_mobily_transfer, null);
                        bottomSheetDialog.setContentView(parentView);
                        BottomSheetBehavior bottomSheetBehavior = new BottomSheetBehavior();
                        bottomSheetBehavior.setPeekHeight(
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100 , getResources().getDisplayMetrics()));
                        bottomSheetDialog.show();

                        final Button BtnAskAmountMobily = (Button) parentView.findViewById(R.id.BtnAskAmountMobily);

                        final Button BtnSendAmountMobily = (Button) parentView.findViewById(R.id.BtnSendAmountMobily);

                        final EditText ETAmountMobily;
                        ETAmountMobily = (EditText) parentView.findViewById(R.id.ETAmountMobily);
                        ETAmountMobily.setVisibility(View.GONE);


                        final Button BtnCallSMSMobily = (Button) parentView.findViewById(R.id.BtnCallSMSMobily);
                        BtnCallSMSMobily.setVisibility(View.GONE);


                        BtnAskAmountMobily.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                if(PhoneNumber2.length() > 0){
                                    clipString = ClipData.newPlainText("",PhoneNumber2);
                                    ClipToCopy.setPrimaryClip(clipString);
                                    Toast.makeText(getApplicationContext(),getString(R.string.CopyNumber), Toast.LENGTH_SHORT).show();
                                }

                                CallNumber = "*122#";

                                MakeCall();

                            }
                        });

                        BtnSendAmountMobily.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                ETAmountMobily.setVisibility(View.VISIBLE);
                                BtnCallSMSMobily.setVisibility(View.VISIBLE);
                                BtnSendAmountMobily.setVisibility(View.GONE);
                                BtnAskAmountMobily.setVisibility(View.GONE);


                            }
                        });



                        BtnCallSMSMobily.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                        if (ETAmountMobily.getText().length() == 0) {
                                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.EnterAmount), Toast.LENGTH_SHORT).show();
                                        }

                                        else {


                                            AmountSMS = ETAmountMobily.getText().toString();

                                            if(AmountSMS.equals("5")|| AmountSMS.equals("10") || AmountSMS.equals("15") || AmountSMS.equals("20")){

                                                CallNumber = "*123*" + PhoneNumber2.replaceAll(Pattern.quote("+966"), "0") + AmountSMS + Hash;

                                                MakeCall();
                                            }

                                            else {
                                                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.WriteCorrect), Toast.LENGTH_SHORT).show();
                                                ETAmountMobily.setText(null);
                                            }



                                        }


                                }
                        });

                    }
                    break;

            }

        }



    }



    public void PickPhoneNumberForCallme (){

        if (CheckForRadioButton == 0) {

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.CheckRadioButton), Toast.LENGTH_SHORT).show();

        } else if (CheckForRadioButton == 1) {

            Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(i, PICK_CONTACT_FOR_CALLME);

        }
    }



    public void PickPhoneNumberForTransfer(){

        if (CheckForRadioButton == 0) {

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.CheckRadioButton), Toast.LENGTH_SHORT).show();

        } else if (CheckForRadioButton == 1) {

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.ChooseNumberToAskOrToSend), Toast.LENGTH_SHORT).show();

            Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(i, PICK_CONTACT_FOR_TRANSFER);

        }
    }


    String ZainService = "959";
    String MobilyService = "1100";
    String STCService = "900";

    public void Service(){

        if (CheckForRadioButton == 0) {

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.CheckRadioButton), Toast.LENGTH_SHORT).show();

        } else if (CheckForRadioButton == 1) {

            int selectedId = RadioGroup.getCheckedRadioButtonId(); // TO CHECK WHICH RADIO BUTTON IS CHECKED

            switch (selectedId) {

                case R.id.RBZain:
                    if (RBZain.isChecked()) {
                        //Zain Service
                        CallNumber = ZainService;
                        MakeCall();
                    }
                    break;

                case R.id.RBSTC:
                    if (RBSTC.isChecked()) {
                        //STC Service
                        CallNumber = STCService;
                        MakeCall();
                    }
                    break;

                case R.id.RBMobily:
                    if (RBMobily.isChecked()) {

                        //Mobily Service
                        CallNumber = MobilyService;
                        MakeCall();

                    }
                    break;


                default:
                    break;
            }





        }
    }



    final int REQUEST_CALL = 111;

    public void MakeCall(){

        // هذا الشرط لطلب السماح بالوصول للاتصال واختيار الشريحة

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+Uri.encode(CallNumber))));
        }

    }



    ImageView NumberImage;
    String PhoneNumber;
    final int PICK_CONTACT_FOR_CALLME = 555;
    final int PICK_CONTACT_FOR_TRANSFER = 666;



    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        // TO GET NUMBER FROM CONTACT -----------------------------------------------------------



        if (requestCode == PICK_CONTACT_FOR_CALLME && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            PhoneNumber = cursor.getString(column).replaceAll(" ","");

            Callme(PhoneNumber);


        }





        if (requestCode == PICK_CONTACT_FOR_TRANSFER && resultCode == RESULT_OK) {

            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            PhoneNumber = cursor.getString(column).replaceAll(" ","");

           Transfer(PhoneNumber);



        }
        //---------------------------------------------------------------------------------------------------------------------------




        // SPEECH TO TEXT -----------------------------------------------------------------------------------------------------------


        if (requestCode == REQUEST_VOICE && resultCode == RESULT_OK && data != null) {


            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String VoiceCardNumber = result.get(0).replaceAll(" ","");

            ETCardNumber.setText(VoiceCardNumber);


        }



        //-------------------------------------------------------------------------------------------------------------------------




        //For Camera and read the text ----------------------------------------------------------------------------------------------


        switch (requestCode) {

            case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    /* Parameters of runCropImage = File, Activity Context,
                    Image is Scalable or Not, Aspect Ratio X, Aspect Ratio Y, Button Bar Color, Background Color */
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), MainActivity.this, true,
                            5, 1, R.color.white, R.color.white);


                }
                break;


            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, MainActivity.this);
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), MainActivity.this, true,
                            5, 1, R.color.white, R.color.white);

                }
                break;

            case CroperinoConfig.REQUEST_CROP_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri i = Uri.fromFile(CroperinoFileUtil.getTempFile());
                    NumberImage.setImageURI(i);
                    Read();
                    //Do saving / uploading of photo method here.
                    //The image file can always be retrieved via CroperinoFileUtil.getTempFile()
                }
                break;

            default:
                break;
        }

        //-------------------------------------------------------------------------------------------------------------------------------------


    }



    public void Style(int styleNumber){

        SharedPreferences.Editor editor = Shared.edit();
        editor.putInt("StyleNumber",styleNumber);
        editor.apply();

        startActivity(new Intent(this, MainActivity.class));
        CustomIntent.customType(this, "fadein-to-fadeout"); // ANIMATION BETWEEN INTENTS
        finish();

    }



    public void onClick(View v) {



        switch (v.getId()) {

            case R.id.BtnMic:
                Mic();
                break;

            case R.id.BtnCamAndGallary:
                CamAndGallary();
                break;

            case R.id.BtnCallme:
                PickPhoneNumberForCallme();
                break;

            case R.id.BtnBalance:
                Balance();
                break;

            case R.id.BtnServiceCenter:
                Service();
                break;

            case R.id.BtnBalancetransfer:
                PickPhoneNumberForTransfer();
                break;

            case R.id.BtnCharge:
                Charge();
                break;


            default:
                break;

        }

    }

}


