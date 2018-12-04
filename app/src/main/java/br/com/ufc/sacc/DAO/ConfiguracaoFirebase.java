package br.com.ufc.sacc.DAO;

import android.support.annotation.NonNull;
import android.util.Log;
import br.com.ufc.sacc.Model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ConfiguracaoFirebase {
    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;
    private static FirebaseDatabase firebaseDatabase;
//    private static Usuario usuarioLogado = new Usuario();
//    private static String emailAlunoLogado;

    public static DatabaseReference getFirebase(){
        if(referenciaFirebase == null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static FirebaseAuth getAutenticacaoFirebase(){
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static FirebaseDatabase getFirebaseDatabase(){
        if(firebaseDatabase == null){
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }
        return firebaseDatabase;
    }

//    public static Usuario getUsuarioLogado() {
//        firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
//        referenciaFirebase = ConfiguracaoFirebase.getFirebase();
//        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
//
//        emailAlunoLogado = autenticacao.getCurrentUser().getEmail();
//        Log.d("Email do cara logado:", emailAlunoLogado);
//
//
//        referenciaFirebase.child("usuario").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot objSnap : dataSnapshot.getChildren()) {
//                    Usuario usuario = objSnap.getValue(Usuario.class);
//
//                    if(usuario.getEmail().equals(emailAlunoLogado)) {
//                        usuarioLogado.setId(usuario.getId());
//                        usuarioLogado.setNome(usuario.getNome());
//                        usuarioLogado.setRegistro(usuario.getRegistro());
//                        usuarioLogado.setEmail(usuario.getEmail());
//                        usuarioLogado.setSenha(usuario.getSenha());
//                        usuarioLogado.setSexo(usuario.getSexo());
//                        usuarioLogado.setTipo(usuario.getTipo());
//                        Log.d("ID do cara do logado:", usuario.getId());
//                        Log.d("Nome do cara do logado:", usuario.getNome());
//                        Log.d("Registro do logado:", usuario.getRegistro());
//                        Log.d("Email do logado:", usuario.getEmail());
//                        Log.d("Senha do cara logado:", usuario.getSenha());
//                        Log.d("Sexo do cara logado:", usuario.getSexo());
//                        Log.d("Tipo do cara logado:", usuario.getTipo());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//        return usuarioLogado;
//    }

}
