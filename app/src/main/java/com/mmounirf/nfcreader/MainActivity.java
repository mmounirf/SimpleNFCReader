package com.mmounirf.nfcreader;

        import android.app.Activity;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.nfc.NfcAdapter;
        import android.nfc.NfcManager;
        import android.nfc.Tag;
        import android.nfc.tech.IsoDep;
        import android.nfc.tech.MifareClassic;
        import android.nfc.tech.MifareUltralight;
        import android.nfc.tech.Ndef;
        import android.nfc.tech.NfcA;
        import android.nfc.tech.NfcB;
        import android.nfc.tech.NfcF;
        import android.nfc.tech.NfcV;
        import android.os.Bundle;

        import android.widget.TextView;
        import android.widget.Toast;


public class MainActivity extends Activity {



    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NfcManager nfcMan = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = nfcMan.getDefaultAdapter();
        if(adapter == null){
            Toast.makeText(this, "Your device doesn't support NFC", Toast.LENGTH_LONG).show();
        }else{

            if(!adapter.isEnabled()){
                Toast.makeText(this, "Your device support NFC, but it's not enabled", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Your device support NFC, and it's enabled", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);




        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            ((TextView)findViewById(R.id.nfc_value2)).setText(
                    "NFC Tag\n" +
                            ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));


        }

    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }



}