/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 4.1.1 on 2026-06-05 05:26:33.

export interface Account extends BaseEntity {
    holder: Holder;
    accountCards: AccountCard[];
    transactions: Transaction[];
}

export interface QAccount extends EntityPathBase<Account> {
    _super: QBaseEntity;
    accountCards: ListPath<AccountCard, QAccountCard>;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    holder: QHolder;
    id: ComparablePath<string>;
    transactions: ListPath<Transaction, QTransaction>;
    updatedAt: DateTimePath<Date>;
}

export interface AccountCard extends BaseEntity {
    account: Account;
    transactions: Transaction[];
}

export interface QAccountCard extends EntityPathBase<AccountCard> {
    _super: QBaseEntity;
    account: QAccount;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    transactions: ListPath<Transaction, QTransaction>;
    updatedAt: DateTimePath<Date>;
}

export interface Address extends BaseEntity {
    holders: HolderAddress[];
}

export interface QAddress extends EntityPathBase<Address> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    holders: ListPath<HolderAddress, QHolderAddress>;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface CreateCategoryRequest {
    name: string;
}

export interface Category extends BaseEntity {
    name: string;
    journal: Journal;
}

export interface QCategory extends EntityPathBase<Category> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    journal: QJournal;
    name: StringPath;
    updatedAt: DateTimePath<Date>;
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
    addresses: HolderAddress[];
    accounts: Account[];
}

export interface QHolder extends EntityPathBase<Holder> {
    _super: QBaseEntity;
    accounts: ListPath<Account, QAccount>;
    addresses: ListPath<HolderAddress, QHolderAddress>;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    email: StringPath;
    id: ComparablePath<string>;
    password: StringPath;
    updatedAt: DateTimePath<Date>;
}

export interface HolderAddress {
    id: HolderAddressId;
    holder: Holder;
    address: Address;
}

export interface HolderAddressId extends Serializable {
    idHolder: string;
    idAddress: string;
}

export interface QHolderAddress extends EntityPathBase<HolderAddress> {
    address: QAddress;
    holder: QHolder;
    id: QHolderAddressId;
}

export interface QHolderAddressId extends BeanPath<HolderAddressId> {
    idAddress: ComparablePath<string>;
    idHolder: ComparablePath<string>;
}

export interface CreateJournalRequest {
    name: string;
    totalValue: number;
}

export interface GetJournalRequest {
    id: string;
}

export interface UpdateJournalRequest {
    id: string;
    name: string;
    totalValue: number;
}

export interface Journal extends BaseEntity {
    name: string;
    totalValue: number;
    categories: Category[];
    ledgerEntries: LedgerEntry[];
}

export interface QJournal extends EntityPathBase<Journal> {
    _super: QBaseEntity;
    categories: ListPath<Category, QCategory>;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    ledgerEntries: ListPath<LedgerEntry, QLedgerEntry>;
    name: StringPath;
    totalValue: NumberPath<number>;
    updatedAt: DateTimePath<Date>;
}

export interface LedgerEntry extends BaseEntity {
    name: string;
    finalDate: Date;
    journal: Journal;
    transaction: Transaction;
}

export interface QLedgerEntry extends EntityPathBase<LedgerEntry> {
    _super: QBaseEntity;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    finalDate: DateTimePath<Date>;
    id: ComparablePath<string>;
    journal: QJournal;
    name: StringPath;
    transaction: QTransaction;
    updatedAt: DateTimePath<Date>;
}

export interface QTransaction extends EntityPathBase<Transaction> {
    _super: QBaseEntity;
    account: QAccount;
    accountCard: QAccountCard;
    createdAt: DateTimePath<Date>;
    createdBy: StringPath;
    id: ComparablePath<string>;
    updatedAt: DateTimePath<Date>;
}

export interface Transaction extends BaseEntity {
    account: Account;
    accountCard: AccountCard;
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

export interface ListPath<E, Q> extends CollectionPathBase<E[], E, Q>, ListExpression<E, Q> {
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

export interface NumberPath<T> extends NumberExpression<T>, Path<T> {
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

export interface CollectionPathBase<C, E, Q> extends CollectionExpressionBase<C, E>, Path<C> {
}

export interface ListExpression<E, Q> extends CollectionExpression<E[], E> {
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

export interface NumberExpression<T> extends ComparableExpressionBase<T> {
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

export interface CollectionExpressionBase<T, E> extends DslExpression<T>, CollectionExpression<T, E> {
    elementType: Class<E>;
}

export interface CollectionExpression<T, E> extends ParameterizedExpression<T> {
}

export interface TemporalExpression<T> extends LiteralExpression<T> {
}

export interface ComparableExpressionBase<T> extends SimpleExpression<T> {
}

export interface DslExpression<T> extends Expression<T> {
}

export interface ParameterizedExpression<T> extends Expression<T> {
}

export type PathType = "ARRAYVALUE" | "ARRAYVALUE_CONSTANT" | "COLLECTION_ANY" | "DELEGATE" | "LISTVALUE" | "LISTVALUE_CONSTANT" | "MAPVALUE" | "MAPVALUE_CONSTANT" | "PROPERTY" | "VARIABLE" | "TREATED_PATH";
