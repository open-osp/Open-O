package ca.ontario.health.edt;

import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    private static final QName _Info_QNAME;
    private static final QName _Upload_QNAME;
    private static final QName _DownloadResponse_QNAME;
    private static final QName _Download_QNAME;
    private static final QName _List_QNAME;
    private static final QName _Submit_QNAME;
    private static final QName _Update_QNAME;
    private static final QName _InfoResponse_QNAME;
    private static final QName _GetTypeList_QNAME;
    private static final QName _UploadResponse_QNAME;
    private static final QName _SubmitResponse_QNAME;
    private static final QName _GetTypeListResponse_QNAME;
    private static final QName _ListResponse_QNAME;
    private static final QName _Delete_QNAME;
    private static final QName _UpdateResponse_QNAME;
    private static final QName _DeleteResponse_QNAME;
    
    public UploadResponse createUploadResponse() {
        return new UploadResponse();
    }
    
    public Upload createUpload() {
        return new Upload();
    }
    
    public List createList() {
        return new List();
    }
    
    public ResponseResult createResponseResult() {
        return new ResponseResult();
    }
    
    public SubmitResponse createSubmitResponse() {
        return new SubmitResponse();
    }
    
    public GetTypeList createGetTypeList() {
        return new GetTypeList();
    }
    
    public ResourceResult createResourceResult() {
        return new ResourceResult();
    }
    
    public Detail createDetail() {
        return new Detail();
    }
    
    public UploadData createUploadData() {
        return new UploadData();
    }
    
    public Delete createDelete() {
        return new Delete();
    }
    
    public GetTypeListResponse createGetTypeListResponse() {
        return new GetTypeListResponse();
    }
    
    public Download createDownload() {
        return new Download();
    }
    
    public CsnData createCsnData() {
        return new CsnData();
    }
    
    public DownloadResponse createDownloadResponse() {
        return new DownloadResponse();
    }
    
    public Info createInfo() {
        return new Info();
    }
    
    public InfoResponse createInfoResponse() {
        return new InfoResponse();
    }
    
    public DetailData createDetailData() {
        return new DetailData();
    }
    
    public Update createUpdate() {
        return new Update();
    }
    
    public CommonResult createCommonResult() {
        return new CommonResult();
    }
    
    public DownloadResult createDownloadResult() {
        return new DownloadResult();
    }
    
    public TypeListData createTypeListData() {
        return new TypeListData();
    }
    
    public UpdateRequest createUpdateRequest() {
        return new UpdateRequest();
    }
    
    public TypeListResult createTypeListResult() {
        return new TypeListResult();
    }
    
    public UpdateResponse createUpdateResponse() {
        return new UpdateResponse();
    }
    
    public ListResponse createListResponse() {
        return new ListResponse();
    }
    
    public DownloadData createDownloadData() {
        return new DownloadData();
    }
    
    public Submit createSubmit() {
        return new Submit();
    }
    
    public DeleteResponse createDeleteResponse() {
        return new DeleteResponse();
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "info")
    public JAXBElement<Info> createInfo(final Info value) {
        return (JAXBElement<Info>)new JAXBElement(ObjectFactory._Info_QNAME, (Class)Info.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "upload")
    public JAXBElement<Upload> createUpload(final Upload value) {
        return (JAXBElement<Upload>)new JAXBElement(ObjectFactory._Upload_QNAME, (Class)Upload.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "downloadResponse")
    public JAXBElement<DownloadResponse> createDownloadResponse(final DownloadResponse value) {
        return (JAXBElement<DownloadResponse>)new JAXBElement(ObjectFactory._DownloadResponse_QNAME, (Class)DownloadResponse.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "download")
    public JAXBElement<Download> createDownload(final Download value) {
        return (JAXBElement<Download>)new JAXBElement(ObjectFactory._Download_QNAME, (Class)Download.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "list")
    public JAXBElement<List> createList(final List value) {
        return (JAXBElement<List>)new JAXBElement(ObjectFactory._List_QNAME, (Class)List.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "submit")
    public JAXBElement<Submit> createSubmit(final Submit value) {
        return (JAXBElement<Submit>)new JAXBElement(ObjectFactory._Submit_QNAME, (Class)Submit.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "update")
    public JAXBElement<Update> createUpdate(final Update value) {
        return (JAXBElement<Update>)new JAXBElement(ObjectFactory._Update_QNAME, (Class)Update.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "infoResponse")
    public JAXBElement<InfoResponse> createInfoResponse(final InfoResponse value) {
        return (JAXBElement<InfoResponse>)new JAXBElement(ObjectFactory._InfoResponse_QNAME, (Class)InfoResponse.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "getTypeList")
    public JAXBElement<GetTypeList> createGetTypeList(final GetTypeList value) {
        return (JAXBElement<GetTypeList>)new JAXBElement(ObjectFactory._GetTypeList_QNAME, (Class)GetTypeList.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "uploadResponse")
    public JAXBElement<UploadResponse> createUploadResponse(final UploadResponse value) {
        return (JAXBElement<UploadResponse>)new JAXBElement(ObjectFactory._UploadResponse_QNAME, (Class)UploadResponse.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "submitResponse")
    public JAXBElement<SubmitResponse> createSubmitResponse(final SubmitResponse value) {
        return (JAXBElement<SubmitResponse>)new JAXBElement(ObjectFactory._SubmitResponse_QNAME, (Class)SubmitResponse.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "getTypeListResponse")
    public JAXBElement<GetTypeListResponse> createGetTypeListResponse(final GetTypeListResponse value) {
        return (JAXBElement<GetTypeListResponse>)new JAXBElement(ObjectFactory._GetTypeListResponse_QNAME, (Class)GetTypeListResponse.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "listResponse")
    public JAXBElement<ListResponse> createListResponse(final ListResponse value) {
        return (JAXBElement<ListResponse>)new JAXBElement(ObjectFactory._ListResponse_QNAME, (Class)ListResponse.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "delete")
    public JAXBElement<Delete> createDelete(final Delete value) {
        return (JAXBElement<Delete>)new JAXBElement(ObjectFactory._Delete_QNAME, (Class)Delete.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "updateResponse")
    public JAXBElement<UpdateResponse> createUpdateResponse(final UpdateResponse value) {
        return (JAXBElement<UpdateResponse>)new JAXBElement(ObjectFactory._UpdateResponse_QNAME, (Class)UpdateResponse.class, (Class)null, (Object)value);
    }
    
    @XmlElementDecl(namespace = "http://edt.health.ontario.ca/", name = "deleteResponse")
    public JAXBElement<DeleteResponse> createDeleteResponse(final DeleteResponse value) {
        return (JAXBElement<DeleteResponse>)new JAXBElement(ObjectFactory._DeleteResponse_QNAME, (Class)DeleteResponse.class, (Class)null, (Object)value);
    }
    
    static {
        _Info_QNAME = new QName("http://edt.health.ontario.ca/", "info");
        _Upload_QNAME = new QName("http://edt.health.ontario.ca/", "upload");
        _DownloadResponse_QNAME = new QName("http://edt.health.ontario.ca/", "downloadResponse");
        _Download_QNAME = new QName("http://edt.health.ontario.ca/", "download");
        _List_QNAME = new QName("http://edt.health.ontario.ca/", "list");
        _Submit_QNAME = new QName("http://edt.health.ontario.ca/", "submit");
        _Update_QNAME = new QName("http://edt.health.ontario.ca/", "update");
        _InfoResponse_QNAME = new QName("http://edt.health.ontario.ca/", "infoResponse");
        _GetTypeList_QNAME = new QName("http://edt.health.ontario.ca/", "getTypeList");
        _UploadResponse_QNAME = new QName("http://edt.health.ontario.ca/", "uploadResponse");
        _SubmitResponse_QNAME = new QName("http://edt.health.ontario.ca/", "submitResponse");
        _GetTypeListResponse_QNAME = new QName("http://edt.health.ontario.ca/", "getTypeListResponse");
        _ListResponse_QNAME = new QName("http://edt.health.ontario.ca/", "listResponse");
        _Delete_QNAME = new QName("http://edt.health.ontario.ca/", "delete");
        _UpdateResponse_QNAME = new QName("http://edt.health.ontario.ca/", "updateResponse");
        _DeleteResponse_QNAME = new QName("http://edt.health.ontario.ca/", "deleteResponse");
    }
}
