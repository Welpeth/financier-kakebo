/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 4.1.1 on 2026-06-04 00:59:05.

export interface Account extends BaseEntity {
}

export interface QAccount extends EntityPathBase<Account> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface AccountCard extends BaseEntity {
}

export interface QAccountCard extends EntityPathBase<AccountCard> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface Address {
    id: string;
}

export interface QAddress extends EntityPathBase<Address> {
    id: ComparablePath<string>;
}

export interface Category extends BaseEntity {
}

export interface QCategory extends EntityPathBase<Category> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface Journal extends BaseEntity {
}

export interface QJournal extends EntityPathBase<Journal> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface LedgerEntry extends BaseEntity {
}

export interface QLedgerEntry extends EntityPathBase<LedgerEntry> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface QTransaction extends EntityPathBase<Transaction> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface Transaction extends BaseEntity {
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
}

export interface Holder extends BaseEntity {
    password: string;
    email: string;
}

export interface QHolder extends EntityPathBase<Holder> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    email: StringPath;
    id: ComparablePath<string>;
    password: StringPath;
    updatedAt: DateTimePath<Date>;
}

export interface HolderAddress {
    id: HolderAddressId;
    user: Holder;
    address: Address;
}

export interface HolderAddressId extends Serializable {
    idHolder: string;
    idAddress: string;
}

export interface QHolderAddress extends EntityPathBase<HolderAddress> {
    address: QAddress;
    id: QHolderAddressId;
    user: QHolder;
}

export interface QHolderAddressId extends BeanPath<HolderAddressId> {
    idAddress: ComparablePath<string>;
    idHolder: ComparablePath<string>;
}

export interface BaseEntity {
    id: string;
    createdAt: Date;
    createdBy: string;
    updatedAt: Date;
}

export interface QBaseEntity extends EntityPathBase<BaseEntity> {
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface DateTimePath<T> extends DateTimeExpression<T>, Path<T> {
}

export interface StringPath extends StringExpression, Path<string> {
}

export interface ComparablePath<T> extends ComparableExpression<T>, Path<T> {
}

export interface Path<T> extends Expression<T> {
    root: Path<any>;
    metadata: PathMetadata;
    annotatedElement: AnnotatedElement;
}

export interface PathMetadata extends Serializable {
    element: any;
    parent: Path<any>;
    rootPath: Path<any>;
    pathType: PathType;
    name: string;
    root: boolean;
}

export interface AnnotatedElement {
    annotations: Annotation[];
    declaredAnnotations: Annotation[];
}

export interface Class<T> extends Serializable, GenericDeclaration, Type, AnnotatedElement, OfField<Class<any>>, Constable {
}

export interface Serializable {
}

export interface StringExpression extends LiteralExpression<string> {
}

export interface Annotation {
}

export interface GenericDeclaration extends AnnotatedElement {
    typeParameters: TypeVariable<any>[];
}

export interface Type {
    typeName: string;
}

export interface Constable {
}

export interface EntityPathBase<T> extends BeanPath<T>, EntityPath<T> {
}

export interface BeanPath<T> extends SimpleExpression<T>, Path<T> {
}

export interface DateTimeExpression<T> extends TemporalExpression<T> {
}

export interface ComparableExpression<T> extends ComparableExpressionBase<T> {
}

export interface Expression<T> extends Serializable {
    type: Class<T>;
}

export interface TypeVariable<D> extends Type, AnnotatedElement {
    genericDeclaration: D;
    annotatedBounds: AnnotatedType[];
    name: string;
    bounds: Type[];
}

export interface OfField<F> extends TypeDescriptor {
    array: boolean;
    primitive: boolean;
}

export interface LiteralExpression<T> extends ComparableExpression<T> {
}

export interface AnnotatedType extends AnnotatedElement {
    annotatedOwnerType: AnnotatedType;
    type: Type;
}

export interface TypeDescriptor {
}

export interface EntityPath<T> extends Path<T> {
}

export interface SimpleExpression<T> extends DslExpression<T> {
}

export interface TemporalExpression<T> extends LiteralExpression<T> {
}

export interface ComparableExpressionBase<T> extends SimpleExpression<T> {
}

export interface DslExpression<T> extends Expression<T> {
}

export type PathType = "ARRAYVALUE" | "ARRAYVALUE_CONSTANT" | "COLLECTION_ANY" | "DELEGATE" | "LISTVALUE" | "LISTVALUE_CONSTANT" | "MAPVALUE" | "MAPVALUE_CONSTANT" | "PROPERTY" | "VARIABLE" | "TREATED_PATH";
