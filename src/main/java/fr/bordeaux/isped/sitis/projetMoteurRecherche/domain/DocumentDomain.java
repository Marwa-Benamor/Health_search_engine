package fr.bordeaux.isped.sitis.projetMoteurRecherche.domain;

public class DocumentDomain {
    private int docId;

    private String docTitle;

    private   String docContent;

    private   String docLink;


    public DocumentDomain() {
    }

    public int getDocId() {
        return docId;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public String getDocContent() {
        return docContent;
    }

    public String getDocLink() {
        return docLink;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public void setDocContent(String docContent) {
        this.docContent = docContent;
    }

    public void setDocLink(String docLink) {
        this.docLink = docLink;
    }

}
